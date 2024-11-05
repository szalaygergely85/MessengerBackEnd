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

   @PostMapping
   public ResponseEntity<?> addMessage(
      @RequestBody Message message,
      @RequestHeader("Authorization") String token
   ) {
      if (
         _permissionService.hasPermissionToConversation(
            token,
            message.getConversationId()
         )
      ) {
         return ResponseEntity
            .ok()
            .body(_messageService.createMessage(message));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("/get-messages")
   public ResponseEntity<?> getConversationMessages(
           @RequestHeader("Authorization") String token,
           @RequestParam("conversationId") Long conversationId
   ) {
      if (_permissionService.hasPermissionToConversation(token, conversationId)) {
         return ResponseEntity
                 .ok()
                 .body(
                         _messageService.getMessagesByConversationIdOrderedByTimestamp(conversationId)
                 );
      } else return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body("Unauthorized");
   }

   @GetMapping("/validate")
   public ResponseEntity<?> getMessagesAndCompareWithLocal(
      @RequestHeader("Authorization") String token,
      @RequestParam("count") int count
   ) {
      if (_permissionService.isUserRegistered(token)) {
         return ResponseEntity
            .ok()
            .body(_messageService.getMessagesAndCompareWithLocal(token, count));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("new-message")
   public ResponseEntity<?> getNewMessagesByUserToken(
      @RequestHeader("Authorization") String authToken
   ) {
      List<Message> messages = _messageService.getNewMessagesByUserToken(
         authToken
      );
      if (_permissionService.hasPermissionToMessages(authToken, messages)) {
         return ResponseEntity.ok().body(messages);
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PatchMapping("/mark-as-downloaded")
   public ResponseEntity<String> markMessagesAsDownloaded(
      @RequestBody List<Long> messageIds
   ) {
      List<Message> messages = _messageService.markMessagesAsDownloaded(
         messageIds
      );
      if (messages.isEmpty()) {
         return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("No messages found with the provided IDs");
      }

      return ResponseEntity.ok("Messages marked as downloaded");
   }

   @Autowired
   MessageController(
      MessageService _messageService,
      PermissionService _permissionService
   ) {
      this._messageService = _messageService;
      this._permissionService = _permissionService;
   }

   private final MessageService _messageService;
   private final PermissionService _permissionService;
}
