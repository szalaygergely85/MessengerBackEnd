package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.message.entity.MessageToSend;
import com.gege.ideas.messenger.message.repository.MessageToSendRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageToSendService {

   private final MessageToSendRepository messageToSendRepository;

   @Autowired
   public MessageToSendService(
      MessageToSendRepository messageToSendRepository
   ) {
      this.messageToSendRepository = messageToSendRepository;
   }

   public MessageToSend createMessageTo(MessageToSend messageToSend) {
      return messageToSendRepository.save(messageToSend);
   }

   public List<MessageToSend> getNotDeliveredMessages(Long userId) {
      return messageToSendRepository.findByUserIdAndDeliveredFalse(userId);
   }

   @Transactional
   public void markMessageAsDelivered(Long messageId, Long userId) {
      // Fetch the message by its ID
      MessageToSend message = messageToSendRepository.findByMessageIdAndUserId(
         messageId,
         userId
      );

      System.out.println(message.toString());
      // Set the delivered flag to true
      message.setDelivered(true);

      // Save the updated message
      messageToSendRepository.save(message);
   }
}
