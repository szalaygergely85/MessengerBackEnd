package com.gege.ideas.messenger.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessageService {

   public String sendDataMessage(String token, Map<String, String> data) {
      try {
         Message message = Message
            .builder()
            .setToken(token)
            .putAllData(data) // <-- add key-value pairs here
            .build();

         // Send the message
         return FirebaseMessaging.getInstance().send(message);
      } catch (Exception e) {
         e.printStackTrace();
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

         // Send the message
         String response = FirebaseMessaging.getInstance().send(message);
         return response; // FCM returns a message ID
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}
