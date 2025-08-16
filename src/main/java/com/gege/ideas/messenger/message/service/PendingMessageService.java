package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.message.entity.PendingMessage;
import com.gege.ideas.messenger.message.repository.PendingMessageRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PendingMessageService {

   private final PendingMessageRepository pendingMessageRepository;
   private final UserService userService;

   @Autowired
   public PendingMessageService(
      UserService userService,
      PendingMessageRepository pendingMessageRepository
   ) {
      this.userService = userService;
      this.pendingMessageRepository = pendingMessageRepository;
   }

   public PendingMessage createPendingMessage(PendingMessage pendingMessage) {
      PendingMessage localPendingMessage =
         pendingMessageRepository.findByUuidAndUserId(
            pendingMessage.getUuid(),
            pendingMessage.getUserId()
         );
      if (localPendingMessage != null) {
         return localPendingMessage;
      }
      return pendingMessageRepository.save(pendingMessage);
   }

   public List<PendingMessage> getNotDeliveredMessages(String token) {
      return pendingMessageRepository.findByUserIdAndDeliveredFalse(
         userService.getUserIdByToken(token)
      );
   }

   @Transactional
   public void markMessageAsDelivered(String uuid, String token) {
      // Fetch the message by its ID
      User user = userService.getUserByToken(token);

      PendingMessage message = pendingMessageRepository.findByUuidAndUserId(
         uuid,
         user.getUserId()
      );

      if (message != null) {
         System.out.println(message.toString());
         // Set the delivered flag to true
         message.setDelivered(true);

         // Save the updated message
         pendingMessageRepository.save(message);
      }
   }

   public void deletePendingMessages(String uuid) {
      List<PendingMessage> pendingMessages =
         pendingMessageRepository.findByUuid(uuid);
      for (PendingMessage pendingMessage : pendingMessages) {
         pendingMessageRepository.delete(pendingMessage);
      }
   }
}
