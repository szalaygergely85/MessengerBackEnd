package com.gege.ideas.messenger.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FirebaseInitializer {

   @Value("${firebase.service-account.path}")
   private String serviceAccountPath;

   @PostConstruct
   public void init() throws IOException {
      FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

      FirebaseOptions options = new FirebaseOptions.Builder()
         .setCredentials(GoogleCredentials.fromStream(serviceAccount))
         .build();

      FirebaseApp.initializeApp(options);
   }
}
