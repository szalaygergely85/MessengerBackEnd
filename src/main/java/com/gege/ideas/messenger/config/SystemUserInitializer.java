package com.gege.ideas.messenger.config;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.HashUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemUserInitializer {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserService userService;

   private static final String AD_EMAIL = "admin@zenvy.com";
   private static final String AD_DISPLAY_NAME = "Admin";
   private static final String AD_PASSWORD = HashUtil.hashPassword("123456");
   private static final String AD_TOKEN = "123456";
   private static final String AD_UUID = "ad_123456";

   private static final String SYSTEM_EMAIL = "websocket@zenvy.com";
   private static final String SYSTEM_DISPLAY_NAME = "websocket";
   private static final String SYSTEM_PASSWORD = "123456";
   private static final String SYSTEM_TOKEN = "123456";
   private static final String SYSTEM_UUID = "user_123456";

   private static final String TEST_EMAIL = "test@zenvy.com";
   private static final String TEST_DISPLAY_NAME = "test";
   private static final String TEST_PASSWORD = "123456";
   public static final String TEST_UUID = "test_123456";
   private static final String TEST_TOKEN = "test_123456";

   private static final String TEST_PUBLIC_KEY = "test_123456";

   private static final Long TEST_ID = 9999L;

   @PersistenceContext
   private EntityManager entityManager;

   @PostConstruct
   public void createSystemUserIfNotExists() throws Exception {
      if (!userRepository.existsByEmail(SYSTEM_EMAIL)) {
         User user = new User();
         user.setEmail(SYSTEM_EMAIL);
         user.setPassword(SYSTEM_PASSWORD);
         user.setDisplayName(SYSTEM_DISPLAY_NAME);
         user.setUuid(SYSTEM_UUID);
         user.setToken(SYSTEM_TOKEN);
         userRepository.save(user);

         System.out.println("✅ System user created.");
      } else {
         System.out.println("ℹ️ System user already exists.");
      }
      if (!userRepository.existsByEmail(TEST_EMAIL)) {
         User testUser = new User();
         testUser.setEmail(TEST_EMAIL);
         testUser.setPassword(TEST_PASSWORD);
         testUser.setDisplayName(TEST_DISPLAY_NAME);
         testUser.setUuid(TEST_UUID);
         testUser.setToken(TEST_TOKEN);
         testUser.setPublicKey(TEST_PUBLIC_KEY);
         testUser.setUserId(TEST_ID);
         userRepository.save(testUser);

         System.out.println("✅ Test user created.");
      } else {
         System.out.println("ℹ️ Test user already exists.");
      }

      if (!userRepository.existsByEmail(AD_EMAIL)) {
         User testUser = new User();
         testUser.setEmail(AD_EMAIL);
         testUser.setPassword(AD_PASSWORD);
         testUser.setDisplayName(AD_DISPLAY_NAME);
         testUser.setUuid(AD_UUID);
         testUser.setToken(AD_TOKEN);
         userRepository.save(testUser);

         System.out.println("✅ Admin user created.");
      } else {
         System.out.println("ℹ️ Admin user already exists.");
      }
   }
}
