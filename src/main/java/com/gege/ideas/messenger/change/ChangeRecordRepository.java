package com.gege.ideas.messenger.change;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeRecordRepository
   extends JpaRepository<ChangeRecord, Long> {
   Optional<ChangeRecord> findByUserIdAndEntityType(
      Long userId,
      Long entityType
   );

   List<ChangeRecord> findByUserId(Long userId);
}
