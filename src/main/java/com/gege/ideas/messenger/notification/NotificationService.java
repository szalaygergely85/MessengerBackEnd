package com.gege.ideas.messenger.notification;

import java.util.Map;

public interface NotificationService {
   String sendNotification(
      long userId,
      Map<String, String> data,
      long conversationId
   );
}
