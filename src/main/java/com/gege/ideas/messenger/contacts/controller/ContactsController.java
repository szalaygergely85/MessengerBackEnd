package com.gege.ideas.messenger.contacts.controller;

import com.gege.ideas.messenger.contacts.entity.Contact;
import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.service.UserService;
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

   @GetMapping("/validate")
   public ResponseEntity<?> getContactsAndCompareWithLocal(
      @RequestHeader("Authorization") String authToken,
      @RequestParam("count") int count
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(
               contactsService.getContactsAndCompareWithLocal(authToken, count)
            );
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("/get-contact/{id}")
   public ResponseEntity<?> getContactById(
           @PathVariable Long id,
           @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
                 .ok()
                 .body(contactsService.getContactById(id));
      } else return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body("Unauthorized");
   }

   @GetMapping("/get-contacts")
   public ResponseEntity<?> searchContacts(

      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(contactsService.getContacts(authToken));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PostMapping("/add-contact")
   public ResponseEntity<?> addContact(
      @RequestBody Contact contact,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.hasPermissionToAddContact(authToken, contact.getOwnerId())) {
         return ResponseEntity
            .ok()
            .body(contactsService.addContact(contact));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }
}
