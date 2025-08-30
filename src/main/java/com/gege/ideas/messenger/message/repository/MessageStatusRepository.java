package com.gege.ideas.messenger.message.repository;

import com.gege.ideas.messenger.message.entity.MessageStatus;
import com.gege.ideas.messenger.message.entity.MessageStatusType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageStatusRepository
   extends JpaRepository<MessageStatus, Long> {
   List<MessageStatus> findByUuid(String uuid);
   MessageStatus findByUuidAndUserId(String uuid, Long userId);
   List<MessageStatus> findByUserIdAndMessageStatusType(
      Long userId,
      MessageStatusType messageStatusType
   );
}
