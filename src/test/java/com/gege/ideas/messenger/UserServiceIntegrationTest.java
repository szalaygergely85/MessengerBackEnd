package com.gege.ideas.messenger;

import static org.junit.jupiter.api.Assertions.*;

import com.gege.ideas.messenger.user.repository.UserRepository;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.user.service.UserTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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
   /*
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

@Test
public void testAddUser() {

	User testUser = new User(
			"Doe",
			"John",
			"john.doe@example.com",
			"password123",
			null,
			1234567890L
	);

	UserToken userToken = userService.addUser(testUser);

	UserToken mockUserToken = new UserToken();

	// Assertions
	assertNotNull(mockUserToken);
	assertNotNull(mockUserToken.getUserTokenId());
	assertEquals("testUser", userRepository.findByUserId(user.getUserId()).getUsername());

}*/

}
