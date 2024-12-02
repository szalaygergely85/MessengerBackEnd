package com.gege.ideas.messenger.change;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeRecordService {
    private final ChangeRecordRepository repository;

    @Autowired
    public ChangeRecordService(ChangeRecordRepository repository) {
        this.repository = repository;
    }

    // Update the last modified time for a specific user and entity type
    public void updateChange(Long userId, Long entityType) {
        long currentTimestamp = System.currentTimeMillis();
        ChangeRecord changeRecord = repository.findByUserIdAndEntityType(userId, entityType)
                .orElse(new ChangeRecord());

        changeRecord.setUserId(userId);
        changeRecord.setEntityType(entityType);
        changeRecord.setLastModified(currentTimestamp);

        repository.save(changeRecord);
    }

    // Get changes for a user
    public List<ChangeRecord> getChangesForUser(Long userId) {
        return repository.findByUserId(userId);
    }

    // Check if any entity has changed since a given timestamp
    public boolean hasChanges(Long userId, Long sinceTimestamp) {
        return repository.findByUserId(userId).stream()
                .anyMatch(record -> record.getLastModified() > sinceTimestamp);
    }
}
