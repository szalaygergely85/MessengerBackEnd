package com.gege.ideas.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gege.ideas.messenger.contacts.repository.ContactsRepository;
import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ContactsServiceIntegrationTest {

   @MockBean
   private ContactsRepository contactsRepository;

   @MockBean
   private UserService userService;

   @Autowired
   private ContactsService contactsService;

   private User user1;
   private User user2;
   /*
	@BeforeEach
	void setUp() {

		user1 = new User(1L, "John Doe");
		user2 = new User(2L, "Jane Doe");

		contact1 = new Contacts(1L, 2L); // ownerId = 1, contactId = 2
		contact2 = new Contacts(1L, 3L); // ownerId = 1, contactId = 3

		when(userService.getUser(2L)).thenReturn(user1);
		when(userService.getUser(3L)).thenReturn(user2);
	}

	@Test
	void testGetContactUserByOwnerId() {
		when(contactsRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(contact1, contact2));

		List<User> contactUsers = contactsService.getContactUserByOwnerId(1L);

		assertEquals(2, contactUsers.size());
		assertEquals("John Doe", contactUsers.get(0).getName());
		assertEquals("Jane Doe", contactUsers.get(1).getName());
	}

	@Test
	void testSearchContacts() {
		when(userService.searchUsers("Doe")).thenReturn(Arrays.asList(user1, user2));

		List<User> contactUsers = contactsService.searchContacts("Doe");

		assertEquals(2, contactUsers.size());
		assertEquals("John Doe", contactUsers.get(0).getName());
		assertEquals("Jane Doe", contactUsers.get(1).getName());
	}

	@Test
	void testAddContact() {
		Contacts contact = new Contacts(1L, 2L);

		when(contactsRepository.save(contact)).thenReturn(contact);

		Boolean isAdded = contactsService.addContact(1L, 2L);

		assertTrue(isAdded);
	}

	@Test
	void testAddContactFailure() {
		Contacts contact = new Contacts(1L, 2L);

		when(contactsRepository.save(contact)).thenReturn(null);

		Boolean isAdded = contactsService.addContact(1L, 2L);

		assertFalse(isAdded);
	}*/
}
