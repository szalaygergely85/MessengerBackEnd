package com.gege.ideas.messenger.user.controller;

import com.gege.ideas.messenger.DTO.LoginRequest;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

   private final UserService userService;

   @Autowired
   public UserController(UserService userService) {
      this.userService = userService;
   }

   @PostMapping("/login")
   public UserToken logInUser(@RequestBody LoginRequest loginRequest) {
      return userService.logInUser(
         loginRequest.getEmail(),
         loginRequest.getPassword()
      );
   }

   @PostMapping
   public UserToken putUser(@RequestBody User user) throws Exception {
      return userService.addUser(user);
   }

   @GetMapping("/id/{id}")
   public User getUser(@PathVariable Long id) {
      return userService.getUserById(id);
   }

   @GetMapping("/token/{token}")
   public User getUser(@PathVariable String token) {
      return userService.getUserByToken(token);
   }

   @GetMapping("/key/{token}")
   public String getKeyByToken(@PathVariable String token) throws Exception {
      return userService.getKeyByToken(token);
   }

   @GetMapping("/publickey/{userId}")
   public String getPublicKeyByUerId(@PathVariable Long userId) {
      return userService.getPublicKeyByToken(userId);
   }
}
