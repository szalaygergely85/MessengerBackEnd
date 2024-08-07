package com.gege.ideas.messenger.notifcation.repository;

import com.gege.ideas.messenger.notifcation.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
   extends JpaRepository<Notification, Long> {
   List<Notification> findByUserIdAndIsActiveTrue(Long userId);
}
