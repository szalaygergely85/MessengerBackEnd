package com.gege.ideas.messenger.conversation.controller;

import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.security.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversation")
public class ConversationsController {

   private final ConversationService conversationService;

   private final PermissionService _permissionService;

   @Autowired
   public ConversationsController(
      ConversationService conversationService,
      PermissionService _permissionService
   ) {
      this.conversationService = conversationService;
      this._permissionService = _permissionService;
   }

   @PostMapping("add-conversation/conversation")
   public ResponseEntity<?> addConversation(
      @RequestBody List<User> participants,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isInParticipants(participants, authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(conversationService.addConversation(participants, authToken));
   }

   @PostMapping("add-conversation/user-ids")
   public ResponseEntity<?> addConversationById(
      @RequestBody List<Long> participantsId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isInParticipantsIds(participantsId, authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(
            conversationService.addConversationById(participantsId, authToken)
         );
   }

   @GetMapping("get-conversation/{id}")
   public ResponseEntity<?> getConversationAndContentById(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.hasPermissionToConversation(authToken, id)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(conversationService.getConversationDTOById(id));
   }

   @GetMapping("validate")
   public ResponseEntity<?> getConversationAndCompareWithLocal(
      @RequestParam("count") int count,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(
            conversationService.getConversationAndCompareWithLocal(
               count,
               authToken
            )
         );
   }

   @GetMapping("get-conversations")
   public ResponseEntity<?> getConversationsByToken(
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(conversationService.getConversationsByAuthToken(authToken));
   }

   @DeleteMapping("remove-conversation/{conversationId}")
   public ResponseEntity<?> deleteConversation(
      @PathVariable Long conversationId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isUserTestUser(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(conversationService.clearConversation(conversationId));
   }
   /*
@GetMapping("validate-participant")
public ResponseEntity<?> getConversationParticipantAndCompareWithLocal(
	@RequestParam("count") Long count,
	@RequestHeader("Authorization") String authToken
) {
	if (permissionService.isUserRegistered(authToken)) {
		return ResponseEntity
			.ok()
			.body(
			conversationService.getConversationParticipantAndCompareWithLocal(
				count,
				authToken
			)
			);
	} else return ResponseEntity
		.status(HttpStatus.UNAUTHORIZED)
		.body("Unauthorized");
}*/
}
