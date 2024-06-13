package com.gege.ideas.messenger.message.service;

import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.repository.MessageRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.user.service.UserTokenService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

   private final MessageRepository messageRepository;
   private final UserTokenService userTokenService;
   private final UserService userService;

   private final ConversationParticipantsService conversationParticipantsService;

   @Autowired
   public MessageService(
      MessageRepository messageRepository,
      UserTokenService userTokenService,
      UserService userService,
      ConversationParticipantsService conversationParticipantsService
   ) {
      this.messageRepository = messageRepository;
      this.userTokenService = userTokenService;
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
   }

   public List<MessageBoard> getLatestMessage(String token) {
      User user = userService.getUserByToken(token);
      if (user != null) {
         List<MessageBoard> messageBoard = new ArrayList<>();
         List<Long> conversationIds =
            conversationParticipantsService.getConversationIdsByUserId(
               user.getUserId()
            );
         for (int i = 0; i < conversationIds.size(); i++) {
            List<User> participants =
               conversationParticipantsService.getUsersByConversationId(
                  conversationIds.get(i)
               );
            Message message =
               messageRepository.findTopByConversationIdOrderByTimestampDesc(
                  conversationIds.get(i)
               );
            if (message != null) {
               messageBoard.add(
                  new MessageBoard(
                     conversationIds.get(i),
                     message,
                     participants
                  )
               );
            }
         }
         return messageBoard;
      }
      return null;
   }

   public Message createMessage(Message message) {
      message.setRead(false);
      return messageRepository.save(message);
   }

   public List<Message> getConversationMessages(long id) {
      return messageRepository.findByConversationIdOrderByTimestampAsc(id);
   }
}
