package com.gege.ideas.messenger.utils.test;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestUtil {

   private UserService userService;

   @Autowired
   public UserTestUtil(UserService userService) {
      this.userService = userService;
   }

   public UserToken createTestUser() throws Exception {
      User user = new User();
      user.setEmail(RandomUtil.getRandomString(10));
      user.setPassword(RandomUtil.getRandomString(10));
      user.setDisplayName(RandomUtil.getRandomString(10));
      user.setFullName(RandomUtil.getRandomString(10));
      user.setPhoneNumber(RandomUtil.getRandomLong());
      return userService.addUser(user);
   }

   public void deleteUser(User user) {
      userService.deleteUser(user);
   }

   public User getUser(String authToken) {
      return userService.getUserByToken(authToken);
   }
}
