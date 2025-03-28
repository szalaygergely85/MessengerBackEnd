package com.gege.ideas.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gege.ideas.messenger.contacts.repository.ContactsRepository;
import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ContactServiceIntegrationTest {

   @MockBean
   private ContactsRepository contactsRepository;

   @MockBean
   private UserService userService;

   @Autowired
   private ContactsService contactsService;

   private User user1;
   private User user2;

   @BeforeEach
   void setUp() {}
}
