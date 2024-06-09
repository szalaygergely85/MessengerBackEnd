package com.gege.ideas.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.user.service.UserTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTest {

   @Autowired
   private UserService userService;

   @MockBean
   private UserRepository userRepository;

   @MockBean
   private UserTokenService userTokenService;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void testLogInUser_Success() {
      String email = "test@example.com";
      String password = "password123";
      User mockUser = new User();
      mockUser.setEmail(email);
      mockUser.setPassword(password);
      mockUser.setUserTokenId(1L);

      UserToken mockUserToken = new UserToken();
      mockUserToken.setUserTokenId(1L);

      when(userRepository.findByEmail(email)).thenReturn(mockUser);
      when(userTokenService.getUserTokenByTokenId(anyLong()))
         .thenReturn(mockUserToken);

      UserToken result = userService.logInUser(email, password);

      assertNotNull(result);
      assertEquals(mockUserToken.getUserTokenId(), result.getUserTokenId());
   }

   @Test
   public void testLogInUser_WithIncorrectPassword_ReturnsNull() {

      String email = "test@example.com";
      String password = "password123";
      User user = new User();
      user.setEmail(email);
      user.setPassword("wrongPassword");
      when(userRepository.findByEmail(email)).thenReturn(user);

      UserToken userToken = userService.logInUser(email, password);

      assertEquals(null, userToken);
   }

   @Test
   public void testLogInUser_WithNonExistentUser_ReturnsNull() {
      String email = "nonexistent@example.com";
      String password = "password123";

      when(userRepository.findByEmail(email)).thenReturn(null);

      UserToken userToken = userService.logInUser(email, password);

      assertEquals(null, userToken);
   }
}
