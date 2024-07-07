package com.gege.ideas.messenger.notifcation.controller;

import com.gege.ideas.messenger.notifcation.entity.Notification;
import com.gege.ideas.messenger.notifcation.service.NotificationService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

   @GetMapping
   public List<Notification> getActiveNotifications(
      @RequestHeader("Authorization") String token
   ) {
      return _notificationService.getActiveNotifications(token);
   }

   @Autowired
   public NotificationController(
      PermissionService _permissionService,
      NotificationService _notificationService
   ) {
      this._permissionService = _permissionService;
      this._notificationService = _notificationService;
   }

   private final PermissionService _permissionService;

   private final NotificationService _notificationService;
}
