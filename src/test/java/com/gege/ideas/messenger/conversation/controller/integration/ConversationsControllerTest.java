package com.gege.ideas.messenger.conversation.controller.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // JUnit4
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversationsControllerTest {
   /*
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
String UserToken authToken;
String UserToken authToken2;
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
	if (conversation != null) {
		conversationService.clearConversation(
			conversation.getConversationId()
		);
	}
}

@Ignore
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
	assertEquals(testUser.getUserId(), conversation.getCreatorUserId());
	assertEquals(participants.size(), conversation.getNumberOfParticipants());
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
}*/
}
