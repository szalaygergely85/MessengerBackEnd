package com.gege.ideas.messenger.conversation.service;

import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import com.gege.ideas.messenger.conversation.repository.ConversationParticipantsRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationParticipantsService {

   private ConversationParticipantsRepository conversationParticipantsRepository;
   private UserService userService;

   @Autowired
   public ConversationParticipantsService(
      ConversationParticipantsRepository conversationParticipantsRepository,
      UserService userService
   ) {
      this.conversationParticipantsRepository =
      conversationParticipantsRepository;
      this.userService = userService;
   }

   public List<Long> getParticipantIds(Long conversationId) {
      List<ConversationParticipant> conversationParticipants =
         conversationParticipantsRepository.findByConversationId(
            conversationId
         );
      List<Long> participantIds = new ArrayList<>();
      for (ConversationParticipant conversationParticipant : conversationParticipants) {
         long userId = conversationParticipant.getUserId();
         participantIds.add(userId);
      }

      return participantIds;
   }

   public List<Long> getConversationIdsByUserId(Long userId) {
      List<ConversationParticipant> conversationParticipants =
         conversationParticipantsRepository.findByUserId(userId);
      List<Long> conversationId = new ArrayList<>();
      for (ConversationParticipant conversationParticipant : conversationParticipants) {
         long id = conversationParticipant.getConversationId();
         conversationId.add(id);
      }

      return conversationId;
   }

   public List<User> getUsersByConversationId(Long conversationId) {
      List<ConversationParticipant> conversationParticipants =
         conversationParticipantsRepository.findByConversationId(
            conversationId
         );
      List<User> users = new ArrayList<>();

      for (ConversationParticipant conversationParticipant : conversationParticipants) {
         User user = userService.getUser(conversationParticipant.getUserId());
         users.add(user);
      }
      return users;
   }

   public void addConversationParticipant(
      ConversationParticipant conversationParticipant
   ) {
      conversationParticipantsRepository.save(conversationParticipant);
   }
}
