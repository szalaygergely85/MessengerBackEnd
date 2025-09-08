package com.gege.ideas.messenger.user.service;

import com.gege.ideas.messenger.firebase.FirebaseMessageService;
import com.gege.ideas.messenger.mail.MailService;
import com.gege.ideas.messenger.tokens.Token;
import com.gege.ideas.messenger.tokens.TokenService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.utils.FileUtil;
import com.gege.ideas.messenger.utils.TokenGeneratorUtil;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

   private final UserRepository userRepository;
   private final FirebaseMessageService firebaseMessageService;
   private final TokenService tokenService;
   private final MailService mailService;

   @Autowired
   public UserService(
      UserRepository userRepository,
      FirebaseMessageService firebaseMessageService,
      TokenService tokenService,
      MailService mailService
   ) {
      this.userRepository = userRepository;
      this.firebaseMessageService = firebaseMessageService;
      this.tokenService = tokenService;
      this.mailService = mailService;
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

   public boolean changePassword(long userId, String password) {
      User user = getUserById(userId);
      user.setPassword(password);
      _update(user);
      return true;
   }

   public User getUserByToken(String token) {
      return userRepository.findByToken(token);
   }

   public Long getUserIdByToken(String token) {
      User user = userRepository.findByToken(token);

      return user.getUserId();
   }

   public String getTokenById(Long id) {
      return userRepository.findById(id).map(User::getToken).orElse(null);
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

   public Object deleteUser(String email) {
      User user = userRepository.findByEmail(email);
      userRepository.delete(user);
      return true;
   }

   public void sendForgotEmail(String email)
      throws MessagingException, UnsupportedEncodingException {
      User user = getUserByEmail(email);
      Token token = tokenService.generateToken(user.getUserId());
      mailService.sendSimpleEmail(
         user.getEmail(),
         "Password Reset Request",
         _getEmailBody(token.getToken())
      );
      setLastEmailSent(user, System.currentTimeMillis());
   }

   private void setLastEmailSent(User user, long timestamp) {
      int count = user.getSentEmailCount();
      user.setLastForgotEmailSent(timestamp);
      user.setSentEmailCount(count + 1);
      userRepository.save(user);
   }

   public User getUserByEmail(String email) {
      return userRepository.findByEmail(email);
   }

   private String _getEmailBody(String token) {
      String body =
         " Hello, <br><br>We received a request to reset your password for your Zenvy account. <br><br>" +
         "To set a new password, please click the link below:<br>" +
         "https://zen-vy.com/forgot-password/" +
         token +
         "<br><br>" +
         "If you did not request a password reset, please ignore this email. Your account will remain secure.<br><br>" +
         "Thank you,<br>The Zenvy Team";
      return body;
   }
}
