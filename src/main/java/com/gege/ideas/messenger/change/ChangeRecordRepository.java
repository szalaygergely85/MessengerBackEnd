package com.gege.ideas.messenger.change;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeRecordRepository extends JpaRepository<ChangeRecord, Long> {
    Optional<ChangeRecord> findByUserIdAndEntityType(Long userId, Long entityType);

    List<ChangeRecord> findByUserId(Long userId);
}
