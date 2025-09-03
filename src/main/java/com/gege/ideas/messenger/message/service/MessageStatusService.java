package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.message.entity.MessageStatus;
import com.gege.ideas.messenger.message.entity.MessageStatusType;
import com.gege.ideas.messenger.message.repository.MessageStatusRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageStatusService {

   private final MessageStatusRepository messageStatusRepository;
   private final UserService userService;

   @Autowired
   public MessageStatusService(
      UserService userService,
      MessageStatusRepository messageStatusRepository
   ) {
      this.userService = userService;
      this.messageStatusRepository = messageStatusRepository;
   }

   public MessageStatus createPendingMessage(MessageStatus messageStatus) {
      MessageStatus status = messageStatusRepository.findByUuid(messageStatus.getUuid())
              .orElseThrow(() -> new RuntimeException("Message status not found for uuid: " +messageStatus.getUuid()));

      if (status != null) {
         return status;
      }
      return messageStatusRepository.save(messageStatus);
   }

   public List<MessageStatus> getMessagesStatusNotDelivered(String token) {
      return messageStatusRepository.findByUserIdAndStatus(
         userService.getUserIdByToken(token),
         MessageStatusType.PENDING
      );
   }

   @Transactional
   public void markMessageAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      User user = userService.getUserByToken(token);

      MessageStatus status = messageStatusRepository.findByUuid(uuid)
              .orElseThrow(() -> new RuntimeException("Message status not found for uuid: " + uuid));


         status.getUserStatuses().put(user.getUserId(), MessageStatusType.DELIVERED);
          status.getDeliveredStatuses().put(user.getUserId(), true);

         // Save the updated message
         messageStatusRepository.save(status);

   }

   @Transactional
   public void markMessageAsRead(String uuid, String token) {
      // Fetch the message by its ID
      User user = userService.getUserByToken(token);

        MessageStatus status = messageStatusRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Message status not found for uuid: " + uuid));


         status.getUserStatuses().put(user.getUserId(), MessageStatusType.READ);
         status.getDeliveredStatuses().put(user.getUserId(), true);
         // Save the updated message
         messageStatusRepository.save(status);

   }

   public void deleteMessageStatus(String uuid) {
      MessageStatus status = messageStatusRepository.findByUuid(uuid)
              .orElseThrow(() -> new RuntimeException("Message status not found for uuid: " + uuid));

         messageStatusRepository.delete(status);

   }

   public MessageStatus getMessageStatus(String uuid, long userId) {
      return messageStatusRepository
              .findByUuidAndUserId(uuid, userId)
              .orElseThrow(() -> new RuntimeException("MessageStatus not found for user " + userId));
   }

   public List<MessageStatus> getMessageStatusByDelivered(String authToken, boolean b) {
      User user = userService.getUserByToken(authToken);
      return messageStatusRepository.findByUserIdAndDelivered(user.getUserId(), b);

   }
}
