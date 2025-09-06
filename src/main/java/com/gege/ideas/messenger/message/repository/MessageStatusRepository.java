package com.gege.ideas.messenger.message.repository;

import com.gege.ideas.messenger.message.entity.MessageStatus;
import com.gege.ideas.messenger.message.entity.MessageStatusType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageStatusRepository
   extends JpaRepository<MessageStatus, Long> {
   Optional<MessageStatus> findByUuid(String uuid);

   @Query(
      "SELECT ms FROM MessageStatus ms JOIN ms.deliveredStatuses ds " +
      "WHERE KEY(ds) = :userId AND ds = :delivered"
   )
   List<MessageStatus> findByUserIdAndDelivered(
      @Param("userId") Long userId,
      @Param("delivered") boolean delivered
   );

   @Query(
      "SELECT ms FROM MessageStatus ms JOIN ms.userStatuses us " +
      "WHERE ms.uuid = :uuid AND KEY(us) = :userId"
   )
   Optional<MessageStatus> findByUuidAndUserId(
      @Param("uuid") String uuid,
      @Param("userId") Long userId
   );

   @Query(
      "SELECT ms FROM MessageStatus ms JOIN ms.userStatuses us " +
      "WHERE KEY(us) = :userId AND (:status IS NULL OR VALUE(us) = :status)"
   )
   List<MessageStatus> findByUserIdAndStatus(
      @Param("userId") Long userId,
      @Param("status") MessageStatusType status
   );
}
