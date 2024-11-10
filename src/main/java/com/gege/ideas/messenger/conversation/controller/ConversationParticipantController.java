package com.gege.ideas.messenger.conversation.controller;

import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversation-participant")
public class ConversationParticipantController {

   private final ConversationParticipantsService conversationParticipantsService;

   private final PermissionService permissionService;

   @Autowired
   public ConversationParticipantController(
      ConversationParticipantsService conversationParticipantsService,
      PermissionService permissionService
   ) {
      this.conversationParticipantsService = conversationParticipantsService;
      this.permissionService = permissionService;
   }

   @GetMapping("get-participants")
   public ResponseEntity<?> getConversationAndCompareWithLocal(
      @RequestParam("conversationId") Long conversationId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(
               conversationParticipantsService.getParticipants(
                  authToken,
                  conversationId
               )
            );
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }
}
