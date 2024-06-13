package com.gege.ideas.messenger.contacts.service;

import com.gege.ideas.messenger.contacts.entity.Contacts;
import com.gege.ideas.messenger.contacts.repository.ContactsRepository;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactsService {

   private ContactsRepository contactsRepository;
   private UserService userService;

   @Autowired
   public ContactsService(
      ContactsRepository contactsRepository,
      UserService userService
   ) {
      this.contactsRepository = contactsRepository;
      this.userService = userService;
   }

   public List<User> getContactUserByOwnerId(Long id) {
      List<User> contactUsers = new ArrayList<>();
      List<Contacts> contactsList = contactsRepository.findByOwnerId(id);
      for (Contacts contact : contactsList) {
         User user = userService.getUserById(contact.getContactUserId());
         contactUsers.add(user);
      }
      return contactUsers;
   }

   public List<User> searchContacts(String search) {
      List<User> contactUsers = userService.searchUsers(search);

      return contactUsers;
   }

   public Boolean addContact(Long ownerId, Long contactId) {
      Contacts contact = new Contacts(ownerId, contactId);

      Contacts savedContact = contactsRepository.save(contact);
      if (savedContact != null) {
         return true;
      }
      return false;
   }
}
