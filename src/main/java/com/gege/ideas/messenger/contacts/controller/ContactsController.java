package com.gege.ideas.messenger.contacts.controller;

import com.gege.ideas.messenger.contacts.entity.Contact;
import com.gege.ideas.messenger.contacts.service.ContactsService;
import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.security.permission.service.PermissionService;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
      if (!permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(
            contactsService.getContactsAndCompareWithLocal(authToken, count)
         );
   }

   @DeleteMapping("/delete-contact")
   public ResponseEntity<?> deleteContactById(
      @RequestParam("ContactUserId") Long userId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.hasPermissionToDeleteContact(authToken, userId)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity
         .ok()
         .body(contactsService.deleteContact(authToken, userId));
   }

   @GetMapping("/get-contact/{id}")
   public ResponseEntity<?> getContactById(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(contactsService.getContactById(id));
   }

   @GetMapping("/get-contacts")
   public ResponseEntity<?> searchContacts(
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(contactsService.getContacts(authToken));
   }

   @PostMapping("/add-contact")
   public ResponseEntity<?> addContact(
      @RequestBody Contact contact,
      @RequestHeader("Authorization") String authToken
   ) {
      if (
         !permissionService.hasPermissionToAddContact(
            authToken,
            contact.getOwnerId()
         )
      ) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(contactsService.addContact(contact));
   }
}
