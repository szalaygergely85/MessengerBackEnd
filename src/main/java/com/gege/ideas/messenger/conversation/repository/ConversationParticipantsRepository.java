package com.gege.ideas.messenger.conversation.repository;

import com.gege.ideas.messenger.conversation.entity.ConversationParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipantsRepository
   extends JpaRepository<ConversationParticipant, Long> {
   List<ConversationParticipant> findByUserId(Long userId);

   List<ConversationParticipant> findByConversationId(Long id);

   void deleteConversationParticipantsByConversationId(Long conversationId);
}
