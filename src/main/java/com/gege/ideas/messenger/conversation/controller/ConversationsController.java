package com.gege.ideas.messenger.conversation.controller;

import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversation")
public class ConversationsController {

   private final ConversationService conversationService;

   private final PermissionService permissionService;

   @Autowired
   public ConversationsController(
      ConversationService conversationService,
      PermissionService permissionService
   ) {
      this.conversationService = conversationService;
      this.permissionService = permissionService;
   }

   @PostMapping("add-conversation/conversation")
   public ResponseEntity<?> addConversation(
      @RequestBody List<User> participants,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isInParticipants(participants, authToken)) {
         return ResponseEntity
            .ok()
            .body(conversationService.addConversation(participants, authToken));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PostMapping("add-conversation/user-ids")
   public ResponseEntity<?> addConversationById(
      @RequestBody List<Long> participantsId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isInParticipantsIds(participantsId, authToken)) {
         return ResponseEntity
            .ok()
            .body(
               conversationService.addConversationById(
                  participantsId,
                  authToken
               )
            );
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("get-conversation/{id}")
   public ResponseEntity<?> getConversationAndContentById(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.hasPermissionToConversation(authToken, id)) {
         return ResponseEntity
            .ok()
            .body(conversationService.getConversationDTOById(id));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("validate")
   public ResponseEntity<?> getConversationAndCompareWithLocal(
      @RequestParam("count") int count,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(
               conversationService.getConversationAndCompareWithLocal(
                  count,
                  authToken
               )
            );
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("get-conversations")
   public ResponseEntity<?> getConversationsByToken(
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(conversationService.getConversationsByAuthToken(authToken));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
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
