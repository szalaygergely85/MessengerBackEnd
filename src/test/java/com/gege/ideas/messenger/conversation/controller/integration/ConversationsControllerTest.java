package com.gege.ideas.messenger.conversation.controller.integration;

import static org.junit.Assert.*;

import com.gege.ideas.messenger.conversation.entity.Conversation;
import com.gege.ideas.messenger.conversation.service.ConversationParticipantsService;
import com.gege.ideas.messenger.conversation.service.ConversationService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.utils.RandomUtil;
import com.gege.ideas.messenger.utils.test.UserTestUtil;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // JUnit4
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversationsControllerTest {

   @Autowired
   private UserTestUtil userTestUtil;

   @Autowired
   private TestRestTemplate restTemplate;

   @Autowired
   ConversationParticipantsService conversationParticipantsService;

   @Autowired
   ConversationService conversationService;

   private User testUser;
   private User testUser2;
   private UserToken authToken;
   private UserToken authToken2;
   private Conversation conversation;

   @BeforeEach
   public void setUp() throws Exception {
      authToken = userTestUtil.createTestUser();
      testUser = userTestUtil.getUser(authToken.getToken());
      authToken2 = userTestUtil.createTestUser();
      testUser2 = userTestUtil.getUser(authToken2.getToken());
   }

   @AfterEach
   public void tearDown() {
      // Clean up the test user after each test
      userTestUtil.deleteUser(testUser);
      userTestUtil.deleteUser(testUser2);
      conversationService.deleteConversation(conversation);
   }

   @Test
   public void testAddConversationByIdConversationCreated() throws Exception {
      List<Long> participantsId = Arrays.asList(
         testUser.getUserId(),
         testUser2.getUserId()
      );

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", authToken.getToken());
      HttpEntity<List<Long>> requestEntity = new HttpEntity<>(
         participantsId,
         headers
      );
      ResponseEntity<?> response = restTemplate.postForEntity(
         "/api/conversation/id",
         requestEntity,
         Long.class
      );

      assertEquals(HttpStatus.OK, response.getStatusCode());

      Long conversationId = (Long) response.getBody();

      assertNotNull(conversationId);

      conversation = conversationService.getConversationById(conversationId);

      List<User> participants =
         conversationParticipantsService.getUsersByConversationId(
            conversationId
         );

      assertEquals(participantsId.size(), participants.size());

      assertTrue(_isInParticipantList(participantsId, participants));

      assertEquals(conversation.getCreatorUserId(), testUser.getUserId());

      assertEquals(participants.size(), conversation.getNumberOfParticipants());
   }

   @Test
   public void testAddConversationByIdUnAuth() throws Exception {
      List<Long> participantsId = Arrays.asList(
         testUser.getUserId(),
         testUser2.getUserId()
      );

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", RandomUtil.getRandomString(10));
      HttpEntity<List<Long>> requestEntity = new HttpEntity<>(
         participantsId,
         headers
      );
      ResponseEntity<?> response = restTemplate.postForEntity(
         "/api/conversation/id",
         requestEntity,
         Long.class
      );

      assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
   }

   private boolean _isInParticipantList(
      List<Long> participantsId,
      List<User> participants
   ) {
      for (User participant : participants) {
         if (!participantsId.contains(participant.getUserId())) {
            return false;
         }
      }
      return true;
   }
}
