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
      List<Notification> notifications =
         notificationRepository.findByUserIdAndIsActiveTrue(
            userService.getUserIdByToken(token)
         );
      _setNotificationInActive(notifications);
      return notifications;
   }

   public Notification addNotification(Notification notification) {
      return notificationRepository.save(notification);
   }

   private void _setNotificationInActive(List<Notification> notifications) {
      for (Notification notification : notifications) {
         notification.setActive(false);
         notificationRepository.save(notification);
      }
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
