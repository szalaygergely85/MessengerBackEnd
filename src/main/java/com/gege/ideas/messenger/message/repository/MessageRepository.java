package com.gege.ideas.messenger.message.repository;

import com.gege.ideas.messenger.message.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
   List<Message> findByConversationIdOrderByTimestampAsc(long id);
   List<Message> findByConversationIdAndTimestampAfterOrderByTimestampAsc(
      long id,
      long timestamp
   );

   @Query(
      value = "SELECT * FROM message " +
      "WHERE conversation_id = :conversationId " +
      "ORDER BY conversation_order_id IS NULL, conversation_order_id DESC " +
      "LIMIT 1",
      nativeQuery = true
   )
   Message findLatestMessageByConversationId(
      @Param("conversationId") Long conversationId
   );

   Message findTopByConversationIdOrderByConversationOrderIdDesc(long id);

   Message findTopByConversationIdOrderByTimestampDesc(long id);

   List<Message> findByConversationId(long id);
   Message findByUuid(String uuid);
}
