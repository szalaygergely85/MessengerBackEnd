package com.gege.ideas.messenger.message.controller;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.PendingMessage;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.message.service.PendingMessageService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pending-message")
public class PendingMessageController {

   @PostMapping("/add-message")
   public ResponseEntity<?> addMessage(
      @RequestBody PendingMessage pendingMessage,
      @RequestHeader("Authorization") String token
   ) {
      if (
         _permissionService.hasPermissionToMessage(
            _messageService.getMessageByUUID(pendingMessage.getUuid()),
            token
         )
      ) {
         return ResponseEntity
            .ok()
            .body(_pendingMessageService.createPendingMessage(pendingMessage));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PostMapping("/{uuid}/delivered")
   public ResponseEntity<?> markAsDelivered(
      @PathVariable String uuid,
      @RequestHeader("Authorization") String authToken
   ) {
      Message message = _messageService.getMessageByUUID(uuid);
      if (message != null) {
         if (
            _permissionService.hasPermissionToMessage(
               _messageService.getMessageByUUID(uuid),
               authToken
            )
         ) {
            _pendingMessageService.markMessageAsDelivered(uuid, authToken);
            return ResponseEntity.noContent().build(); // 204 No Content
         } else return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Unauthorized");
      } else return ResponseEntity
         .status(HttpStatus.NO_CONTENT)
         .body("No message");
   }

   @Autowired
   PendingMessageController(
      MessageService _messageService,
      PermissionService _permissionService,
      PendingMessageService pendingMessageService
   ) {
      this._pendingMessageService = pendingMessageService;
      this._messageService = _messageService;
      this._permissionService = _permissionService;
   }

   private final MessageService _messageService;
   private final PendingMessageService _pendingMessageService;
   private final PermissionService _permissionService;
}
