package com.gege.ideas.messenger.message.controller;

import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.entity.MessageStatus;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.message.service.MessageStatusService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
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
         !_permissionService.hasPermissionToMessage(
            _messageService.getMessageByUUID(messageStatus.getUuid()),
            token
         )
      ) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(_messageStatusService.createPendingMessage(messageStatus));
   }

   @PostMapping("/{uuid}/delivered")
   public ResponseEntity<?> markAsDelivered(
      @PathVariable String uuid,
      @RequestHeader("Authorization") String authToken
   ) {
      Message message = _messageService.getMessageByUUID(uuid);
      if (message == null) {
         return ResponseEntity.noContent().build();
      }
      if (!_permissionService.hasPermissionToMessage(message, authToken)) {
         throw new UnauthorizedException();
      }
      _messageStatusService.markMessageAsDelivered(uuid, authToken);
      return ResponseEntity.noContent().build();
   }

   @PostMapping("/{uuid}/status-delivered")
   public ResponseEntity<?> markStatusAsDelivered(
      @PathVariable String uuid,
      @RequestHeader("Authorization") String authToken
   ) {
      Message message = _messageService.getMessageByUUID(uuid);
      if (message == null) {
         return ResponseEntity.noContent().build();
      }
      if (!_permissionService.hasPermissionToMessage(message, authToken)) {
         throw new UnauthorizedException();
      }
      _messageStatusService.markMessageStatusAsDelivered(uuid, authToken);
      return ResponseEntity.noContent().build();
   }

   @PostMapping("/{uuid}/read")
   public ResponseEntity<?> markAsRead(
      @PathVariable String uuid,
      @RequestHeader("Authorization") String authToken
   ) {
      Message message = _messageService.getMessageByUUID(uuid);
      if (message == null) {
         return ResponseEntity.noContent().build();
      }
      if (!_permissionService.hasPermissionToMessage(message, authToken)) {
         throw new UnauthorizedException();
      }
      _messageStatusService.markMessageAsRead(uuid, authToken);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("get-messages-status/not-delivered")
   public ResponseEntity<?> getNotDeliveredMessages(
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(
            _messageStatusService.getMessageStatusByDelivered(authToken, false)
         );
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
