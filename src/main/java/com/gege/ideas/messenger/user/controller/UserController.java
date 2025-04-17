package com.gege.ideas.messenger.user.controller;

import com.gege.ideas.messenger.DTO.LoginRequest;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

   private final UserService userService;
   private final PermissionService permissionService;

   @Autowired
   public UserController(
      UserService userService,
      PermissionService permissionService
   ) {
      this.userService = userService;
      this.permissionService = permissionService;
   }

   @DeleteMapping("remove-user/id/{id}")
   public ResponseEntity<?> deleteUser(
                                                     @PathVariable Long id,
                                          @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity.ok().body(userService.deleteUser(id));
      } else return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body("Unauthorized");
   }


   @PostMapping
   public ResponseEntity<?> addUser(@RequestBody User user) throws Exception {
      User localUser = userService.addUser(user);
      if (localUser != null) {
         return ResponseEntity.ok().body(localUser);
      } else {
         return ResponseEntity
                 .status(HttpStatus.CONFLICT)
                 .body("User already exists");
      }
   }

   @GetMapping("/id/{id}")
   public ResponseEntity<?> getUser(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity.ok().body(userService.getUserById(id));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("/search/{search}")
   public ResponseEntity<?> searchUser(
           @PathVariable String search,
           @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity.ok().body(userService.search(search));
      } else return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body("Unauthorized");
   }


   @GetMapping("/token/{token}")
   public ResponseEntity<?> getUserByToken(@PathVariable String token) {
      if (permissionService.isUserRegistered(token)) {
         return ResponseEntity.ok().body(userService.getUserByToken(token));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @GetMapping("/publickey/{userId}")
   public ResponseEntity<?> getPublicKeyByUserId(
      @PathVariable Long userId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserRegistered(authToken)) {
         return ResponseEntity
            .ok()
            .body(userService.getPublicKeyByToken(userId));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PatchMapping
   public ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader("Authorization") String authToken){
      if (permissionService.hasPermissionToUser(user.getToken(), authToken)) {
         return ResponseEntity
                 .ok()
                 .body(userService.updateUser(user));
      }  else return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body("Unauthorized");
   }


   @PostMapping(value = "/login")
   public User logInUser(@RequestBody LoginRequest loginRequest) {
      return userService.logInUser(
         loginRequest.getEmail(),
         loginRequest.getPassword()
      );
   }
}
