package com.gege.ideas.messenger.user.controller.integration;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) // JUnit4
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

   @Autowired
   private TestRestTemplate restTemplate;
   /*
	@Test
	public void testGetUserById() {
		Long userId = 1L;

		ResponseEntity<User> response = restTemplate.getForEntity(
				"http://localhost:8080/api/user/id/" + userId, User.class);

		// Assert the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(userId, response.getBody().getUserId());
	}*/
}
