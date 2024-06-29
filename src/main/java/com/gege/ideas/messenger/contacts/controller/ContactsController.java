package com.gege.ideas.messenger.contacts.controller;

import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {

   private final UserService userService;
   private final ContactsService contactsService;
   private final PermissionService permissionService;

   @Autowired
   public ContactsController(
      UserService userService,
      ContactsService contactsService,
      PermissionService permissionService
   ) {
      this.userService = userService;
      this.contactsService = contactsService;
      this.permissionService = permissionService;
   }

   @GetMapping
   public List<User> getContactUsers(
      @RequestHeader("Authorization") String authToken
   ) {
      Long userId = userService.getUserIdByToken(authToken);
      return contactsService.getContactUserByOwnerId(userId);
   }

   @GetMapping("/{search}/search")
   public ResponseEntity<?> searchContacts(
      @PathVariable String search,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(contactsService.searchContacts(search));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PostMapping
   public ResponseEntity<?> addContact(
      @RequestParam Long ownerId,
      Long contactId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.hasPermissionToAddContact(authToken, ownerId)) {
         return ResponseEntity
            .ok()
            .body(contactsService.addContact(ownerId, contactId));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }
}
