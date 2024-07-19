package com.gege.ideas.messenger.conversation.service;

import com.gege.ideas.messenger.DTO.ConversationContent;
import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import com.gege.ideas.messenger.conversation.repository.ConversationsRepository;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.servicelocator.ServiceLocator;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

   private UserService userService;
   private ConversationParticipantsService conversationParticipantsService;
   private ConversationsRepository _conversationsRepository;

   private final ServiceLocator serviceLocator;

   public Long addConversation(List<User> participants, String authToken) {
      Long conversationId = _getExistingConversationId(participants);

      if (conversationId == null) {
         Conversation conversation = new Conversation(
            System.currentTimeMillis(),
            userService.getUserIdByToken(authToken),
            participants.size()
         );
         _conversationsRepository.save(conversation);
         for (User participant : participants) {
            ConversationParticipant conversationParticipant =
               new ConversationParticipant(
                  conversation.getConversationId(),
                  participant.getUserId()
               );
            conversationParticipantsService.addConversationParticipant(
               conversationParticipant
            );
         }
         return conversation.getConversationId();
      }
      return conversationId;
   }

   public Long addConversationById(
      List<Long> participantsId,
      String authToken
   ) {
      return addConversation(
         userService.getUsersByIds(participantsId),
         authToken
      );
   }

   public Conversation getConversationById(Long conversationId) {
      return _conversationsRepository.findConversationByConversationId(
         conversationId
      );
   }

   public ConversationContent getConversationContent(Long conversationId) {
      ConversationContent conversationContent = new ConversationContent(
         conversationId
      );
      conversationContent.setParticipants(
         conversationParticipantsService.getUsersByConversationId(
            conversationId
         )
      );
      MessageService messageService = serviceLocator.getMessageService();
      conversationContent.setMessages(
         messageService.getMessagesByConversationIdOrderedByTimestamp(
            conversationId
         )
      );
      return conversationContent;
   }

   public List<Long> getConversationsWithNewMessage(
      List<Long> conversationIds
   ) {
      List<Long> filteredConversationIds = new ArrayList<>();

      for (Long conversationId : conversationIds) {
         if (_hasNewMessage(conversationId)) {
            filteredConversationIds.add(conversationId);
         }
      }
      return filteredConversationIds;
   }

   private Long _getExistingConversationId(List<User> participants) {
      User user = participants.get(0);

      List<Long> conversationIds =
         conversationParticipantsService.getConversationIdsByUserId(
            user.getUserId()
         );

      for (Long conversationId : conversationIds) {
         List<Long> participantIds =
            conversationParticipantsService.getParticipantIds(conversationId);
         if (participants.size() == participantIds.size()) {
            for (User participant : participants) {
               if (
                  participantIds.contains(participant.getUserId()) &&
                  !participant.equals(participants.get(0))
               ) {
                  return conversationId;
               }
            }
         }
      }

      return null;
   }

   @Deprecated
   private boolean _hasNewMessage(Long conversationId) {
      Conversation conversation =
         _conversationsRepository.findConversationByConversationId(
            conversationId
         );
      return true;
   }

   @Autowired
   public ConversationService(
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      ServiceLocator serviceLocator,
      ConversationsRepository _conversationsRepository
   ) {
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
      this.serviceLocator = serviceLocator;
      this._conversationsRepository = _conversationsRepository;
   }

   public void deleteConversation(Conversation conversation) {
      Long conversationId = conversation.getConversationId();
      conversationParticipantsService.deleteByConversationId(conversationId);
      _conversationsRepository.delete(conversation);
   }
}
