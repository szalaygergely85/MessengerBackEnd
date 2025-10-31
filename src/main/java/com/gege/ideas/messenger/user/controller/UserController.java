package com.gege.ideas.messenger.user.controller;

import com.gege.ideas.messenger.DTO.LoginRequest;
import com.gege.ideas.messenger.exception.ResourceAlreadyExistsException;
import com.gege.ideas.messenger.exception.UnauthorizedException;
import com.gege.ideas.messenger.permission.service.PermissionService;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.DateTimeUtil;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
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
      if (!permissionService.isUserTestUser(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.deleteUser(id));
   }

   @DeleteMapping("remove-user/email/{email}")
   public ResponseEntity<?> deleteUser(
      @PathVariable String email,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.isUserTestUser(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.deleteUser(email));
   }

   @PostMapping
   public ResponseEntity<?> addUser(@RequestBody User user) throws Exception {
      User localUser = userService.addUser(user);
      if (localUser == null) {
         throw new ResourceAlreadyExistsException(
            "Email address already registered"
         );
      }
      return ResponseEntity.ok().body(localUser);
   }

   @GetMapping("/id/{id}")
   public ResponseEntity<?> getUser(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.getUserById(id));
   }

   @GetMapping("/search/{search}")
   public ResponseEntity<?> searchUser(
      @PathVariable String search,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.search(search));
   }

   @GetMapping("/token/{token}")
   public ResponseEntity<?> getUserByToken(@PathVariable String token) {
      if (!permissionService.isUserRegistered(token)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.getUserByToken(token));
   }

   @GetMapping("/publickey/{userId}")
   public ResponseEntity<?> getPublicKeyByUserId(
      @PathVariable Long userId,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.isUserRegistered(authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.getPublicKeyByToken(userId));
   }

   @PatchMapping
   public ResponseEntity<?> updateUser(
      @RequestBody User user,
      @RequestHeader("Authorization") String authToken
   ) {
      if (!permissionService.hasPermissionToUser(user.getToken(), authToken)) {
         throw new UnauthorizedException();
      }
      return ResponseEntity.ok().body(userService.updateUser(user));
   }

   @PostMapping(value = "/login")
   public ResponseEntity<?> logInUser(@RequestBody LoginRequest loginRequest) {
      User user = userService.logInUser(
         loginRequest.getEmail(),
         loginRequest.getPassword()
      );

      System.out.println("🔍 Using email: " + loginRequest.getEmail());
      System.out.println(
         "🔍 Password length: " +
         (loginRequest.getPassword() != null
               ? loginRequest.getPassword().length()
               : "null")
      );
      if (user == null) {
         throw new UnauthorizedException("Invalid email or password");
      }
      return ResponseEntity.ok().body(user);
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
   public ResponseEntity<?> changePassword(
      @RequestParam("old-pass") String oldPassword,
      @RequestParam("new-pass") String newPassword,
      @RequestHeader("Authorization") String authToken
   ) {
      User user = userService.getUserByToken(authToken);
      if (user == null) {
         throw new UnauthorizedException("Invalid authentication token");
      }

      if (!user.getPassword().equals(oldPassword)) {
         throw new UnauthorizedException("Invalid password");
      }

      userService.changePassword(user.getUserId(), newPassword);
      return ResponseEntity.ok().build();
   }

   @GetMapping("/test")
   @ResponseBody
   public String test() {
      return "ok";
   }
}
