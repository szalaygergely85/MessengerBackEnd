package com.gege.ideas.messenger.message.controller;

import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

   private final MessageService messageService;
   private final PermissionService permissionService;

   @Autowired
   MessageController(
      MessageService messageService,
      PermissionService permissionService
   ) {
      this.messageService = messageService;
      this.permissionService = permissionService;
   }

   @GetMapping("/messageboardentries")
   public List<MessageBoard> getLatestMessage(
      @RequestHeader("Authorization") String token
   ) {
      return messageService.getLatestMessage(token);
   }

   @PostMapping
   public ResponseEntity<?> addMessage(
      @RequestBody Message message,
      @RequestHeader("Authorization") String token
   ) {
      if (
         permissionService.hasPermissionToConversation(
            token,
            message.getConversationId()
         )
      ) {
         return ResponseEntity.ok().body(messageService.createMessage(message));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("/byconversationid")
   public ResponseEntity<?> getConversationMessages(
      @RequestParam Long id,
      @RequestHeader("Authorization") String token
   ) {
      if (permissionService.hasPermissionToConversation(token, id)) {
         return ResponseEntity
            .ok()
            .body(messageService.getConversationMessages(id));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }
}
