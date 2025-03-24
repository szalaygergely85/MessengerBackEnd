package com.gege.ideas.messenger.conversation.service;

import com.gege.ideas.messenger.DTO.ConversationDTO;
import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import com.gege.ideas.messenger.conversation.repository.ConversationsRepository;
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

    public ConversationDTO addConversationById(
            List<Long> participantsId,
            String authToken
    ) {
        Long conversationId = addConversation(
                userService.getUsersByIds(participantsId),
                authToken
        );
        return getConversationDTOById(conversationId);
    }

    public Conversation getConversationById(Long conversationId) {
        return _conversationsRepository.findConversationByConversationId(
                conversationId
        );
    }

    public ConversationDTO getConversationDTOById(Long conversationId) {

        ConversationDTO conversationDTO = new ConversationDTO(getConversationById(conversationId),
                conversationParticipantsService.getParticipantsByConversationId(conversationId), conversationParticipantsService.getUsersByConversationId(conversationId));

        return conversationDTO;
    }

    @Deprecated
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

    public void clearConversation(Long conversationId) {
        conversationParticipantsService.deleteByConversationId(conversationId);
        _conversationsRepository.deleteConversationByConversationId(
                conversationId
        );
    }

    public List<Conversation> getConversationAndCompareWithLocal(
            int count,
            String authToken
    ) {
        Long userId = userService.getUserIdByToken(authToken);
        List<Long> conversationIds =
                conversationParticipantsService.getConversationIdsByUserId(userId);

        if (conversationIds.size() == count) {
            return null;
        }

        List<Conversation> conversations = new ArrayList<>();
        for (Long conversationId : conversationIds) {
            conversations.add(
                    _conversationsRepository.findConversationByConversationId(
                            conversationId
                    )
            );
        }
        return conversations;
    }

    public List<ConversationDTO> getConversationsByAuthToken(String authToken) {
        Long userId = userService.getUserIdByToken(authToken);
        List<Long> conversationIds =
                conversationParticipantsService.getConversationIdsByUserId(userId);

        List<ConversationDTO> conversationDTOS = new ArrayList<>();
        for (Long conversationId : conversationIds) {
            conversationDTOS.add(
                    getConversationDTOById(conversationId)

            );
        }
        return conversationDTOS;
    }
}
