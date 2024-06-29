package com.gege.ideas.messenger.conversation.controller;

import com.gege.ideas.messenger.conversation.service.ConversationsService;
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

   private final ConversationsService conversationsService;

   private final PermissionService permissionService;

   @Autowired
   public ConversationsController(
      ConversationsService conversationsService,
      PermissionService permissionService
   ) {
      this.conversationsService = conversationsService;
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
            .body(conversationsService.addConversation(participants));
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
            .body(conversationsService.getConversationContent(id));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }
}
