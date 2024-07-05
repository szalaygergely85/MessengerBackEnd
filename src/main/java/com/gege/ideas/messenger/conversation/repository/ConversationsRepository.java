package com.gege.ideas.messenger.conversation.repository;

import com.gege.ideas.messenger.conversation.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationsRepository
   extends JpaRepository<Conversation, Long> {
   Conversation findConversationByConversationId(Long conversationId);
}
