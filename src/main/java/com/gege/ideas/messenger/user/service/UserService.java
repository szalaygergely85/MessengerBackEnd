package com.gege.ideas.messenger.user.service;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.utils.FileUtil;
import com.gege.ideas.messenger.utils.TokenGeneratorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

   private final UserRepository userRepository;

   @Autowired
   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public User logInUser(String email, String password) {
      User user = userRepository.findByEmail(email);

      if (user != null && user.getPassword().equals(password)) {
         if (user.getToken() == null) {
            user.setToken(TokenGeneratorUtil.generateNewToken());
            return userRepository.save(user);
         }
         return user;
      }
      return null;
   }

   public User addUser(User user) throws Exception {
      if (userRepository.existsByEmail(user.getEmail())) {
         return null;
      }
      user.setToken(TokenGeneratorUtil.generateNewToken());
      user.setLastUpdated(System.currentTimeMillis());
      return userRepository.save(user);
   }

   public User getUserById(Long id) {
      Optional<User> userOptional = userRepository.findById(id);
      if (userOptional.isPresent()) {
         return userOptional.get();
      } else {
         return null;
      }
   }

   public User getUserByToken(String token) {
      return userRepository.findByToken(token);
   }

   public Long getUserIdByToken(String token) {
      User user = userRepository.findByToken(token);

      return user.getUserId();
   }

   public List<User> searchUsers(String search) {
      return userRepository.searchUsers(search);
   }

   private void _update(User user) {
      userRepository.save(user);
   }

   public Resource getKeyByToken(String token) throws Exception {
      Long userId = getUserIdByToken(token);
      return FileUtil.loadAsResource("private_" + userId + ".key");
   }

   public String getPublicKeyByToken(Long userId) {
      User user = getUserById(userId);
      return user.getPublicKey();
   }

   public void deleteUser(User user) {
      userRepository.delete(user);
   }

   public List<User> getUsersByIds(List<Long> participantsId) {
      List<User> users = new ArrayList<>();
      for (Long userId : participantsId) {
         users.add(getUserById(userId));
      }
      return users;
   }

   public User updateUser(User user) {
      user.setLastUpdated(System.currentTimeMillis());
      return userRepository.save(user);
   }

   public List<User> search(String search) {
      return userRepository.searchUsers(search);
   }

   public Boolean deleteUser(Long id) {
      userRepository.deleteById(id);
      return true;
   }
}
