package com.gege.ideas.messenger.contacts.controller;

import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {

   private UserService userService;
   private ContactsService contactsService;

   @Autowired
   public ContactsController(
      UserService userService,
      ContactsService contactsService
   ) {
      this.userService = userService;
      this.contactsService = contactsService;
   }

   @GetMapping("/{token}/byToken")
   public List<User> getContactUsers(@PathVariable String token) {
      Long userId = userService.getUserIdByToken(token);
      return contactsService.getContactUserByOwnerId(userId);
   }

   @GetMapping("/{search}/search")
   public List<User> searchContacts(@PathVariable String search) {
      return contactsService.searchContacts(search);
   }

   @PostMapping
   public Boolean addContact(@RequestParam Long ownerId, Long contactId) {
      return contactsService.addContact(ownerId, contactId);
   }
}
