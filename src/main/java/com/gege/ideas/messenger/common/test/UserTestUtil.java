package com.gege.ideas.messenger.common.test;

import com.gege.ideas.messenger.common.RandomUtil;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestUtil {

   private UserService userService;

   @Autowired
   public UserTestUtil(UserService userService) {
      this.userService = userService;
   }

   public String createTestUser() throws Exception {
      User user = new User();
      user.setEmail(RandomUtil.getRandomString(10));
      user.setPassword(RandomUtil.getRandomString(10));
      user.setDisplayName(RandomUtil.getRandomString(10));
      User newUser = userService.addUser(user);
      return newUser.getToken();
   }

   public void deleteUser(User user) {
      userService.deleteUser(user);
   }

   public User getUser(String authToken) {
      return userService.getUserByToken(authToken);
   }
}
