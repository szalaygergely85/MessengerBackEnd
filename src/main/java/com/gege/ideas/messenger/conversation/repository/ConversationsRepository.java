package com.gege.ideas.messenger.conversation.repository;

import com.gege.ideas.messenger.conversation.entity.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationsRepository
   extends JpaRepository<Conversations, Long> {}
