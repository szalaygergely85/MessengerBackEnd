package com.gege.ideas.messenger.message.controller;

import com.gege.ideas.messenger.DTO.MessageDTO;
import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.message.service.MessageStatusService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

   @PostMapping("/add-message")
   public ResponseEntity<?> addMessage(
      @RequestBody Message message,
      @RequestHeader("Authorization") String token
   ) {
      if (
         !_permissionService.hasPermissionToConversation(
            token,
            message.getConversationId()
         )
      ) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(_messageService.addMessage(message));
   }

   @GetMapping("get-messages/not-delivered")
   public ResponseEntity<?> getNotDeliveredMessages(
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(_messageService.getNotDeliveredMessages(authToken));
   }

   @GetMapping("/get-message/{uuid}")
   public ResponseEntity<?> getConversationMessages(
      @RequestHeader("Authorization") String token,
      @PathVariable String uuid
   ) {
      Message message = _messageService.getMessageByUUID(uuid);
      if (!_permissionService.hasPermissionToMessage(message, token)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(message);
   }

   @GetMapping("/get-messages")
   public ResponseEntity<?> getConversationMessages(
      @RequestHeader("Authorization") String token,
      @RequestParam("conversationId") Long conversationId,
      @RequestParam(value = "timestamp", required = false) Long timestamp
   ) {
      if (
         !_permissionService.hasPermissionToConversation(token, conversationId)
      ) {
         throw new UnauthorizedException();
      }
      List<MessageDTO> messageDTOS =
         _messageService.getMessagesByConversationIdOrderedByTimestamp(
            conversationId,
            timestamp,
            token
         );
      if (messageDTOS.isEmpty()) {
         return ResponseEntity.ok().body(Collections.emptyList());
      }
      return ResponseEntity.ok().body(messageDTOS);
   }

   @GetMapping("/get-latest-message")
   public ResponseEntity<?> getLatestMessage(
      @RequestHeader("Authorization") String token,
      @RequestParam("conversationId") Long conversationId
   ) {
      if (
         !_permissionService.hasPermissionToConversation(token, conversationId)
      ) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(
            _messageService.getLatestMessageByConversationId(conversationId)
         );
   }

   /*
@Deprecated
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

@Deprecated
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
*/

   @PatchMapping("/mark-as-downloaded")
   public ResponseEntity<String> markMessagesAsDownloaded(
      @RequestHeader("Authorization") String authToken,
      @RequestBody List<String> messageUuids
   ) {
      _messageService.markMessagesAsDownloaded(messageUuids, authToken);

      return ResponseEntity.ok("Messages marked as downloaded");
   }

   @DeleteMapping("remove/{uuid}")
   public ResponseEntity<?> deleteMessage(
      @PathVariable String uuid,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!_permissionService.hasPermissionToDeleteMessage(authToken, uuid)) {
         throw new UnauthorizedException();
      }
      _MessageStatusService.deleteMessageStatus(uuid);
      return ResponseEntity.ok().body(_messageService.deleteMessage(uuid));
   }

   @Autowired
   MessageController(
      MessageService _messageService,
      PermissionService _permissionService,
      MessageStatusService messageStatusService
   ) {
      this._messageService = _messageService;
      this._permissionService = _permissionService;
      this._MessageStatusService = messageStatusService;
   }

   private final MessageService _messageService;

   private final PermissionService _permissionService;
   private final MessageStatusService _MessageStatusService;
}
