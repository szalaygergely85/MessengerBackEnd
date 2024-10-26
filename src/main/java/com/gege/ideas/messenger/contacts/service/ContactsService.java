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
   List<User> contactUsers = new ArrayList<>();

   @Autowired
   public ContactsService(
      ContactsRepository contactsRepository,
      UserService userService
   ) {
      this.contactsRepository = contactsRepository;
      this.userService = userService;
   }

   public List<User> getContactUserByOwnerId(Long id) {
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

   public User addContact(Long ownerId, Long contactUserId) {
      if (
         (contactsRepository.findByOwnerIdAndContactUserId(
               ownerId,
               contactUserId
            )).size() ==
         0
      ) {
         Contacts contact = new Contacts(ownerId, contactUserId);
         Contacts savedContact = contactsRepository.save(contact);
         if (savedContact != null) {
            return userService.getUserById(savedContact.getContactUserId());
         }
      }
      return null;
   }

   public Object getContactsAndCompareWithLocal(String authToken, int count) {
      Long userId = userService.getUserIdByToken(authToken);
      List<Contacts> contactsList = contactsRepository.findByOwnerId(userId);

      if (contactsList.size() == count) {
         return null;
      }

      for (Contacts contact : contactsList) {
         User user = userService.getUserById(contact.getContactUserId());
         contactUsers.add(user);
      }
      return contactUsers;
   }
}
