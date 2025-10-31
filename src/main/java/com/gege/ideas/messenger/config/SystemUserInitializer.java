package com.gege.ideas.messenger.config;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.HashUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemUserInitializer {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserService userService;

   @Value("${admin.user.email}")
   private String adminEmail;

   @Value("${admin.user.displayName}")
   private String adminDisplayName;

   @Value("${admin.user.password}")
   private String adminPassword;

   @Value("${admin.user.token}")
   private String adminToken;

   @Value("${admin.user.uuid}")
   private String adminUuid;

   @Value("${system.user.email}")
   private String systemEmail;

   @Value("${system.user.displayName}")
   private String systemDisplayName;

   @Value("${system.user.password}")
   private String systemPassword;

   @Value("${system.user.token}")
   private String systemToken;

   @Value("${system.user.uuid}")
   private String systemUuid;

   @PersistenceContext
   private EntityManager entityManager;

   @PostConstruct
   public void createSystemUserIfNotExists() throws Exception {
      if (!userRepository.existsByEmail(systemEmail)) {
         User user = new User();
         user.setEmail(systemEmail);
         user.setPassword(systemPassword);
         user.setDisplayName(systemDisplayName);
         user.setUuid(systemUuid);
         user.setToken(systemToken);
         userRepository.save(user);

         System.out.println("✅ System user created.");
      } else {
         System.out.println("ℹ️ System user already exists.");
      }

      if (!userRepository.existsByEmail(adminEmail)) {
         User adminUser = new User();
         adminUser.setEmail(adminEmail);
         adminUser.setPassword(HashUtil.hashPassword(adminPassword));
         adminUser.setDisplayName(adminDisplayName);
         adminUser.setUuid(adminUuid);
         adminUser.setToken(adminToken);
         userRepository.save(adminUser);

         System.out.println("✅ Admin user created.");
      } else {
         System.out.println("ℹ️ Admin user already exists.");
      }
   }
}
