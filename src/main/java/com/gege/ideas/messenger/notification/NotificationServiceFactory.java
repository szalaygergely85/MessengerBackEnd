package com.gege.ideas.messenger.notification;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceFactory {

   private final Map<String, NotificationService> services;

   public NotificationServiceFactory(
      Map<String, NotificationService> services
   ) {
      this.services = services;
   }

   public NotificationService getService(String type) {
      return services.get(type + "NotificationService"); // type could be "message", "request", etc.
   }
}
