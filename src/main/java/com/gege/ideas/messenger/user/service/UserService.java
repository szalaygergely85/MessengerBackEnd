package com.gege.ideas.messenger.user.service;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

   private final UserRepository userRepository;
   private final UserTokenService userTokenService;

   @Autowired
   public UserService(
      UserRepository userRepository,
      UserTokenService userTokenService
   ) {
      this.userRepository = userRepository;
      this.userTokenService = userTokenService;
   }

   public UserToken logInUser(String email, String password) {
      User user = userRepository.findByEmail(email);

      if (user != null && user.getPassword().equals(password)) {
         return userTokenService.getUserTokenByTokenId(user.getUserTokenId());
      }
      return null;
   }

   public UserToken addUser(User user) {
      UserToken userToken = userTokenService.createUserToken();
      user.setUserTokenId(userToken.getUserTokenId());
      User savedUser = userRepository.save(user);
      userTokenService.updateUserTokenWithUserId(
         userToken,
         savedUser.getUserId()
      );
      return userToken;
   }

   public User getUser(Long id) {
      Optional<User> userOptional = userRepository.findById(id);
      if (userOptional.isPresent()) {
         return userOptional.get();
      } else {
         return null;
      }
   }

   public User getUser(String token) {
      UserToken userToken = userTokenService.getUserTokenByToken(token);

      return userRepository.findByUserTokenId(userToken.getUserTokenId());
   }

   public Long getUserId(String token) {
      UserToken userToken = userTokenService.getUserTokenByToken(token);
      User user = userRepository.findByUserTokenId(userToken.getUserTokenId());

      return user.getUserId();
   }

   public List<User> searchUsers(String search) {
      return userRepository.searchUsers(search);
   }
}
