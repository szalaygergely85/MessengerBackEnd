package com.gege.ideas.messenger.notifcation.service;

import com.gege.ideas.messenger.notifcation.entity.Notification;
import com.gege.ideas.messenger.notifcation.repository.NotificationRepository;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

   public List<Notification> getActiveNotifications(String token) {
      return notificationRepository.findByUserIdAndIsActiveTrue(
         userService.getUserIdByToken(token)
      );
   }

   public Notification addNotification(Notification notification) {
      return notificationRepository.save(notification);
   }

   @Autowired
   public NotificationService(
      UserService userService,
      NotificationRepository notificationRepository
   ) {
      this.userService = userService;
      this.notificationRepository = notificationRepository;
   }

   private final UserService userService;
   private final NotificationRepository notificationRepository;
}
