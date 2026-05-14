package com.gege.ideas.messenger.firebase;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessageService {

   private static final Logger log = LoggerFactory.getLogger(
      FirebaseMessageService.class
   );

   public String sendDataMessage(String token, Map<String, String> data) {
      try {
         Message message = Message
            .builder()
            .setToken(token)
            .putAllData(data)
            .build();
         return FirebaseMessaging.getInstance().send(message);
      } catch (Exception e) {
         log.error("Failed to send FCM data message to token {}", token, e);
         return null;
      }
   }

   public String sendHighPriorityDataMessage(String token, Map<String, String> data) {
      try {
         Message message = Message
            .builder()
            .setToken(token)
            .putAllData(data)
            .setAndroidConfig(AndroidConfig.builder().setPriority(Priority.HIGH).build())
            .build();
         return FirebaseMessaging.getInstance().send(message);
      } catch (Exception e) {
         log.error("Failed to send high-priority FCM data message to token {}", token, e);
         return null;
      }
   }

   public String sendMessage(String token, String title, String body) {
      try {
         Message message = Message
            .builder()
            .setToken(token)
            .setNotification(
               Notification.builder().setTitle(title).setBody(body).build()
            )
            .build();
         return FirebaseMessaging.getInstance().send(message);
      } catch (Exception e) {
         log.error("Failed to send FCM notification to token {}", token, e);
         return null;
      }
   }
}
