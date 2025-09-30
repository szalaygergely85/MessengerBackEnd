package com.gege.ideas.messenger.message.controller;

import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.MessageStatus;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.message.service.MessageStatusService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message-status")
public class MessageStatusController {

   @PostMapping("/add")
   public ResponseEntity<?> addMessage(
      @RequestBody MessageStatus messageStatus,
      @RequestHeader("Authorization") String token
   ) {
      if (
         _permissionService.hasPermissionToMessage(
            _messageService.getMessageByUUID(messageStatus.getUuid()),
            token
         )
      ) {
         return ResponseEntity
            .ok()
            .body(_messageStatusService.createPendingMessage(messageStatus));
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
            _messageStatusService.markMessageAsDelivered(uuid, authToken);
            return ResponseEntity.noContent().build(); // 204 No Content
         } else return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Unauthorized");
      } else return ResponseEntity
         .status(HttpStatus.NO_CONTENT)
         .body("No message");
   }

   @PostMapping("/{uuid}/status-delivered")
   public ResponseEntity<?> markStatusAsDelivered(
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
            _messageStatusService.markMessageStatusAsDelivered(uuid, authToken);
            return ResponseEntity.noContent().build(); // 204 No Content
         } else return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Unauthorized");
      } else return ResponseEntity
         .status(HttpStatus.NO_CONTENT)
         .body("No message");
   }

   @PostMapping("/{uuid}/read")
   public ResponseEntity<?> markAsRead(
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
            _messageStatusService.markMessageAsRead(uuid, authToken);
            return ResponseEntity.noContent().build(); // 204 No Content
         } else return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Unauthorized");
      } else return ResponseEntity
         .status(HttpStatus.NO_CONTENT)
         .body("No message");
   }

   @GetMapping("get-messages-status/not-delivered")
   public ResponseEntity<?> getNotDeliveredMessages(
      @RequestHeader("Authorization") String authToken
   ) {
      if (_permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(
               _messageStatusService.getMessageStatusByDelivered(
                  authToken,
                  false
               )
            );
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @Autowired
   MessageStatusController(
      MessageService _messageService,
      PermissionService _permissionService,
      MessageStatusService messageStatusService
   ) {
      this._messageStatusService = messageStatusService;
      this._messageService = _messageService;
      this._permissionService = _permissionService;
   }

   private final MessageService _messageService;
   private final MessageStatusService _messageStatusService;
   private final PermissionService _permissionService;
}
