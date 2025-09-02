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
      MessageStatus localMessageStatus =
         messageStatusRepository.findByUuidAndUserId(
            messageStatus.getUuid(),
            messageStatus.getUserId()
         );
      if (localMessageStatus != null) {
         return localMessageStatus;
      }
      return messageStatusRepository.save(messageStatus);
   }

   public List<MessageStatus> getNotDeliveredMessages(String token) {
      return messageStatusRepository.findByUserIdAndMessageStatusType(
         userService.getUserIdByToken(token),
         MessageStatusType.PENDING
      );
   }

   @Transactional
   public void markMessageAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      User user = userService.getUserByToken(token);

      MessageStatus message = messageStatusRepository.findByUuidAndUserId(
         uuid,
         user.getUserId()
      );

      if (message != null) {
         System.out.println(message.toString());
         // Set the delivered flag to true
         message.setMessageStatusType(MessageStatusType.DELIVERED);

         // Save the updated message
         messageStatusRepository.save(message);
      }
   }

   @Transactional
   public void markMessageAsRead(String uuid, String token) {
      // Fetch the message by its ID
      User user = userService.getUserByToken(token);

      MessageStatus message = messageStatusRepository.findByUuidAndUserId(
         uuid,
         user.getUserId()
      );

      if (message != null) {
         System.out.println(message.toString());
         // Set the delivered flag to true
         message.setMessageStatusType(MessageStatusType.READ);

         // Save the updated message
         messageStatusRepository.save(message);
      }
   }

   public void deletePendingMessages(String uuid) {
      List<MessageStatus> messageStatuses = messageStatusRepository.findByUuid(
         uuid
      );
      for (MessageStatus messageStatus : messageStatuses) {
         messageStatusRepository.delete(messageStatus);
      }
   }

   public MessageStatus getMessageStatus(String uuid, long userId) {
      return messageStatusRepository.findByUuidAndUserId(uuid, userId);
   }
}
