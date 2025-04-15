package com.gege.ideas.messenger.message.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gege.ideas.messenger.message.entity.MessageToSend;

public interface MessageToSendRepository
   extends JpaRepository<MessageToSend, Long> {
   MessageToSend findByMessageIdAndUserId(Long messageId, Long userId);
   List<MessageToSend> findByUserIdAndDeliveredFalse(Long userId);
}
