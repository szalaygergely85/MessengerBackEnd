package com.gege.ideas.messenger.message.repository;

import com.gege.ideas.messenger.message.entity.PendingMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingMessageRepository
   extends JpaRepository<PendingMessage, Long> {
   PendingMessage findByUuidAndUserId(String uuid, Long userId);
   List<PendingMessage> findByUserIdAndDeliveredFalse(Long userId);
}
