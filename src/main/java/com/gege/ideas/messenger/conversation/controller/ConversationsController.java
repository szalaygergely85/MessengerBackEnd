package com.gege.ideas.messenger.conversation.controller;

import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

   @PostMapping
   public ResponseEntity<?> addConversation(
      @RequestBody List<User> participants,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isInParticipants(participants, authToken)) {
         return ResponseEntity
            .ok()
            .body(conversationService.addConversation(participants));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("id/{id}")
   public ResponseEntity<?> getConversationAndContentById(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.hasPermissionToConversation(authToken, id)) {
         return ResponseEntity
            .ok()
            .body(conversationService.getConversationContent(id));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }
}
