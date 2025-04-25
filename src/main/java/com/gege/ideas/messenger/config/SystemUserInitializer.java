package com.gege.ideas.messenger.config;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.user.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemUserInitializer {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserService userService;

   private static final String SYSTEM_EMAIL = "websocket@zenvy.com";
   private static final String SYSTEM_DISPLAY_NAME = "websocket";
   private static final String SYSTEM_PASSWORD = "123456";

   @PostConstruct
   public void createSystemUserIfNotExists() throws Exception {
      if (!userRepository.existsByEmail(SYSTEM_EMAIL)) {
         User user = new User();
         user.setEmail(SYSTEM_EMAIL);
         user.setPassword(SYSTEM_PASSWORD);
         user.setDisplayName(SYSTEM_DISPLAY_NAME);
         userService.addUser(user);

         System.out.println("✅ System user created.");
      } else {
         System.out.println("ℹ️ System user already exists.");
      }
   }
}
