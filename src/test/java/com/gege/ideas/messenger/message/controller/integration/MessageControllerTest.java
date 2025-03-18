package com.gege.ideas.messenger.message.controller.integration;

import static org.junit.Assert.*;

import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.message.entity.Message;
import com.gege.ideas.messenger.message.service.MessageService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.utils.test.MessageTestUtil;
import com.gege.ideas.messenger.utils.test.UserTestUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class) // JUnit4
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerTest {

   @Autowired
   private UserTestUtil userTestUtil;

   @Autowired
   private TestRestTemplate restTemplate;

   @Autowired
   private MessageTestUtil messageTestUtil;

   @Autowired
   ConversationService conversationService;

   @Autowired
   MessageService messageService;

   private List<Message> messages = new ArrayList<>();

   private User testUser;
   private User testUser2;
   private UserToken authToken;
   private UserToken authToken2;

   private Long conversationId;

   @BeforeEach
   public void setUp() throws Exception {
      authToken = userTestUtil.createTestUser();
      authToken2 = userTestUtil.createTestUser();
      testUser = userTestUtil.getUser(authToken.getToken());
      testUser2 = userTestUtil.getUser(authToken2.getToken());

      List<User> participants = new ArrayList<>();
      participants.add(testUser);
      participants.add(testUser2);
      conversationService.addConversation(participants, authToken.getToken());
   }

   @AfterEach
   public void tearDown() {
      userTestUtil.deleteUser(testUser);
      if (messages.size() > 0) {
         messageTestUtil.deleteMessages(messages);
      }
      try {
         // Explicitly handle transactions here
         conversationService.clearConversation(testUser.getUserId());
      } catch (Exception e) {
         e.printStackTrace();
         fail("Failed to clear conversations: " + e.getMessage());
      }
   }

   @Ignore
   @Test
   public void testGetMessagesAndCompareWithLocalCorrectValues() {
      int countParam = 5;

      for (int i = 0; i < countParam; i++) {
         messages.add(messageTestUtil.addTestMessage(testUser));
      }

      ResponseEntity<List<Message>> response = _createResponseEntity(10);

      assertEquals(HttpStatus.OK, response.getStatusCode());

      assertEquals(0, response.getBody().size());
   }

   @Ignore
   @Test
   public void testGetMessagesAndCompareWithLocalLessValue() {
      int countParam = 5;

      for (int i = 0; i < countParam + 1; i++) {
         messages.add(messageTestUtil.addTestMessage(testUser, conversationId));
      }

      ResponseEntity<List<Message>> response = _createResponseEntity(
         countParam
      );

      assertEquals(HttpStatus.OK, response.getStatusCode());

      List<Message> responseBody = response.getBody();
      assertNotNull(responseBody);
      assertEquals(countParam + 1, responseBody.size());
   }

   private ResponseEntity<List<Message>> _createResponseEntity(int countParam) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken.getToken());

      // Create HttpEntity with headers
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

      // Define the count parameter dynamically

      // Build the URL with the count parameter
      UriComponentsBuilder uriBuilder = UriComponentsBuilder
         .fromUriString("/api/message/validate")
         .queryParam("count", countParam);
      URI uri = uriBuilder.build().toUri();

      try {
         return restTemplate.exchange(
            uri,
            HttpMethod.GET,
            requestEntity,
            new ParameterizedTypeReference<List<Message>>() {}
         );
      } catch (HttpClientErrorException e) {
         if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
         }
         throw e;
      }
   }
}
