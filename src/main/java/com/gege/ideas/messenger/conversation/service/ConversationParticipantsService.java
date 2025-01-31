package com.gege.ideas.messenger.conversation.service;

import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import com.gege.ideas.messenger.conversation.repository.ConversationParticipantsRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
         User user = userService.getUserById(
            conversationParticipant.getUserId()
         );
         users.add(user);
      }
      return users;
   }

   public void addConversationParticipant(
      ConversationParticipant conversationParticipant
   ) {
      conversationParticipantsRepository.save(conversationParticipant);
   }

   @Transactional
   public void deleteByConversationId(Long conversationId) {
      conversationParticipantsRepository.deleteConversationParticipantsByConversationId(
         conversationId
      );
   }

   public List<ConversationParticipant> getParticipants(
      String authToken,
      Long conversationId
   ) {
      if (conversationId != null) {
         return getParticipantsByConversationId(conversationId);
      } else {
         return getParticipantsByAuthToken(authToken);
      }
   }

   public List<ConversationParticipant> getParticipantsByConversationId(
      Long conversationId
   ) {
      if (conversationId != null) {
         return conversationParticipantsRepository.findByConversationId(
            conversationId
         );
      } else {
         return null;
      }
   }

   public List<ConversationParticipant> getParticipantsByAuthToken(
      String authToken
   ) {
      Long userId = userService.getUserIdByToken(authToken);
      List<Long> conversationIds = getConversationIdsByUserId(userId);

      List<ConversationParticipant> participants = new ArrayList<>();

      for (Long conversationId : conversationIds) {
         List<ConversationParticipant> conversationP =
            getParticipantsByConversationId(conversationId);
         for (ConversationParticipant participant : conversationP) {
            if (participant.getUserId() != userId) {
               participants.add(participant);
            }
         }
      }
      return participants;
   }

   public Object addConversationParticipants(
      List<ConversationParticipant> participants
   ) {
      for (ConversationParticipant participant : participants) {
         conversationParticipantsRepository.save(participant);
      }
      return true;
   }
}
