package com.gege.ideas.messenger.conversation.service;

import com.gege.ideas.messenger.DTO.ConversationContent;
import com.gege.ideas.messenger.DTO.MessageBoard;
import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import com.gege.ideas.messenger.conversation.repository.ConversationsRepository;
import com.gege.ideas.messenger.message.service.MessageService;
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
   private MessageService messageService;
   private ConversationsRepository _conversationsRepository;

   public Long addConversation(List<User> participants) {
      Long conversationId = _getExistingConversationId(participants);
      if (conversationId == null) {
         Conversation conversation = new Conversation(
            System.currentTimeMillis(),
            false
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
      conversationContent.setMessages(
         messageService.getMessagesByConversationIdOrderedByTimestamp(
            conversationId
         )
      );
      return conversationContent;
   }

   public void setConversationHasNewMessage(
      Long conversationId,
      boolean hasNewMessage
   ) {}

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

   private boolean _hasNewMessage(Long conversationId) {
      Conversation conversation =
         _conversationsRepository.findConversationByConversationId(
            conversationId
         );
      return conversation.hasNewMessage();
   }

   @Autowired
   public ConversationService(
      UserService userService,
      ConversationParticipantsService conversationParticipantsService,
      MessageService messageService,
      ConversationsRepository _conversationsRepository
   ) {
      this.userService = userService;
      this.conversationParticipantsService = conversationParticipantsService;
      this.messageService = messageService;
      this._conversationsRepository = _conversationsRepository;
   }
}
