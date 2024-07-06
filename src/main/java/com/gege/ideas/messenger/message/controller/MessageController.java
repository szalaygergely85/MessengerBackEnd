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

   @GetMapping("/byconversationid/{id}")
   public ResponseEntity<?> getConversationMessages(
      @PathVariable Long id,
      @RequestHeader("Authorization") String token
   ) {
      if (_permissionService.hasPermissionToConversation(token, id)) {
         return ResponseEntity
            .ok()
            .body(
               _messageService.getMessagesByConversationIdOrderedByTimestamp(id)
            );
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("/messageboardentries")
   public List<MessageBoard> getLatestMessage(
      @RequestHeader("Authorization") String token
   ) {
      return _messageService.getMessagesBoardEntriesOrderedByTimestamp(token);
   }

   @GetMapping("new-message")
   public ResponseEntity<?> getConversationWithNewMessage(
           @RequestHeader("Authorization") String authToken
   ) {
      List<MessageBoard> messagesBoardEntries = _messageService.getNewMessagesByUserToken(
              authToken
      );
      if (_permissionService.hasPermissionToMessageBoards(authToken, messagesBoardEntries)) {
         return ResponseEntity.ok().body(messagesBoardEntries);
      } else return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body("Unauthorized");
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
