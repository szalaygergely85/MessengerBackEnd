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

   public List<Contacts> getContacts(String authToken, String search) {
      User user = userService.getUserByToken(authToken);

      if (search == null || search.isEmpty()) {
         return contactsRepository.findByOwnerId(user.getUserId());
      }
      List<User> contactUsers = userService.searchUsers(search);

      List<Contacts> contactsList = new ArrayList<>();
      for (User contactUser : contactUsers) {
         Contacts contacts = contactsRepository.findByOwnerIdAndContactUserId(user.getUserId(), contactUser.getUserId());
      }

      return contactsList;
   }

   public Contacts addContact(Contacts contact) {
      if (
         contactsRepository.findByOwnerIdAndContactUserId(
               contact.getOwnerId(),
               contact.getContactUserId()
            ) != null
      ) {
         Contacts savedContact = contactsRepository.save(contact);
         if (savedContact != null) {
            return savedContact;
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

   public Object getContactById(Long id) {
      return contactsRepository.findById(id);
   }
}
