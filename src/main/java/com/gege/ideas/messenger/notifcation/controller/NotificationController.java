package com.gege.ideas.messenger.notifcation.controller;

import com.gege.ideas.messenger.notifcation.NotificationService;
import com.gege.ideas.messenger.notifcation.NotificationServiceFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

   private final NotificationServiceFactory factory;

   @Autowired
   public NotificationController(NotificationServiceFactory factory) {
      this.factory = factory;
   }

   @PostMapping("/send")
   public String sendNotification(
      @RequestParam String type,
      @RequestParam long userId,
      @RequestBody Map<String, String> data
   ) {
      NotificationService service = factory.getService(type);
      if (service == null) {
         throw new IllegalArgumentException("Unknown notification type: " + type);
      }
      return service.sendNotification(userId, data);
   }
}
