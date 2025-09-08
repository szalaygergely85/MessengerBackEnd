package com.gege.ideas.messenger.user.controller;

import com.gege.ideas.messenger.DTO.LoginRequest;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.DateTimeUtil;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
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
      if (permissionService.isUserTestUser(authToken)) {
         return ResponseEntity.ok().body(userService.deleteUser(id));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @DeleteMapping("remove-user/email/{email}")
   public ResponseEntity<?> deleteUser(
      @PathVariable String email,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.isUserTestUser(authToken)) {
         return ResponseEntity.ok().body(userService.deleteUser(email));
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
         Map<String, String> errorResponse = new HashMap<>();
         errorResponse.put("error", "Email address already in registered");
         return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(errorResponse);
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
   public ResponseEntity<?> updateUser(
      @RequestBody User user,
      @RequestHeader("Authorization") String authToken
   ) {
      if (permissionService.hasPermissionToUser(user.getToken(), authToken)) {
         return ResponseEntity.ok().body(userService.updateUser(user));
      } else return ResponseEntity
         .status(HttpStatus.UNAUTHORIZED)
         .body("Unauthorized");
   }

   @PostMapping(value = "/login")
   public ResponseEntity<?> logInUser(@RequestBody LoginRequest loginRequest) {
      User user = userService.logInUser(
         loginRequest.getEmail(),
         loginRequest.getPassword()
      );
      if (user != null) {
         return ResponseEntity.ok().body(user);
      } else {
         Map<String, String> errorResponse = new HashMap<>();
         errorResponse.put("error", "Invalid email or password");
         return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(errorResponse);
      }
   }

   @PostMapping(value = "/forgot-password")
   public ResponseEntity<?> forgotPassword(@RequestParam("email") String email)
      throws MessagingException, UnsupportedEncodingException {
      if (email != null) {
         User user = userService.getUserByEmail(email);
         if (user != null) {
            if (
               user.getSentEmailCount() < 5 &&
               (user.getLastForgotEmailSent() == null ||
                  DateTimeUtil.getDifferenceInMinutesFromNow(
                        user.getLastForgotEmailSent()
                     ) >
                     2)
            ) userService.sendForgotEmail(email);
            return ResponseEntity.status(HttpStatus.OK).build();
         }
      }

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }

   @PostMapping(value = "/change-password")
   public ResponseEntity<?> forgotPassword(
      @RequestParam("old-pass") String oldPassword,
      @RequestParam("new-pass") String newPassword,
      @RequestHeader("Authorization") String authToken
   ) {
      User user = userService.getUserByToken(authToken);
      if (user != null) {
         if (user.getPassword().equals(oldPassword)) {
            userService.changePassword(user.getUserId(), newPassword);
         }

         return ResponseEntity.status(HttpStatus.OK).build();
      }

      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Invalid password");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
   }
}
