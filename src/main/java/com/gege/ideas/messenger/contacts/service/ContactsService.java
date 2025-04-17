package com.gege.ideas.messenger.contacts.service;

import com.gege.ideas.messenger.DTO.ContactsDTO;
import com.gege.ideas.messenger.contacts.entity.Contact;
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
      List<Contact> contactList = contactsRepository.findByOwnerId(id);
      for (Contact contact : contactList) {
         User user = userService.getUserById(contact.getContactUserId());
         contactUsers.add(user);
      }
      return contactUsers;
   }

   public List<ContactsDTO> getContacts(String authToken) {
      User user = userService.getUserByToken(authToken);
      List<ContactsDTO> contactsDTOS = new ArrayList<>();
      List<Contact> contacts = contactsRepository.findByOwnerId(
         user.getUserId()
      );
      for (Contact contact : contacts) {
         contactsDTOS.add(
            new ContactsDTO(
               contact,
               userService.getUserById(contact.getContactUserId())
            )
         );
      }

      return contactsDTOS;
   }

   public Contact addContact(Contact contact) {
      if (
         contactsRepository.findByOwnerIdAndContactUserId(
            contact.getOwnerId(),
            contact.getContactUserId()
         ) ==
         null
      ) {
         Contact savedContact = contactsRepository.save(contact);
         if (savedContact != null) {
            return savedContact;
         }
      }
      return null;
   }

   public Object getContactsAndCompareWithLocal(String authToken, int count) {
      Long userId = userService.getUserIdByToken(authToken);
      List<Contact> contactList = contactsRepository.findByOwnerId(userId);

      if (contactList.size() == count) {
         return null;
      }

      for (Contact contact : contactList) {
         User user = userService.getUserById(contact.getContactUserId());
         contactUsers.add(user);
      }
      return contactUsers;
   }

   public Object getContactById(Long id) {
      return contactsRepository.findById(id);
   }

   public Object deleteContact(String authToken, Long id) {
      User user = userService.getUserByToken(authToken);
      Contact localContact = contactsRepository.findByOwnerIdAndContactUserId(
         user.getUserId(),
         id
      );
      if (localContact != null) {
         contactsRepository.delete(localContact);
      }
      return true;
   }

   public Contact getContactByOwnerIDAndContactUserId(
      Long ownerId,
      Long contactUserId
   ) {
      return contactsRepository.findByOwnerIdAndContactUserId(
         ownerId,
         contactUserId
      );
   }
}
