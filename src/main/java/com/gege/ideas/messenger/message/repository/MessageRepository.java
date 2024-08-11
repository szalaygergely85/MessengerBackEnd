package com.gege.ideas.messenger.message.repository;

import com.gege.ideas.messenger.message.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
   List<Message> findByConversationIdOrderByTimestampAsc(long id);

   Message findTopByConversationIdOrderByTimestampDesc(long id);

   List<Message> findByConversationId(long id);
   List<Message> findByConversationIdAndIsDownloaded(
      long id,
      boolean isDownloaded
   );
}
