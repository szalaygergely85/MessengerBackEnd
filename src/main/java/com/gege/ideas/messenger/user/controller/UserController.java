package com.gege.ideas.messenger.user.controller;

import com.gege.ideas.messenger.DTO.LoginRequest;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

   private final UserService userService;

   @Autowired
   public UserController(UserService userService) {
      this.userService = userService;
   }

   @PostMapping(value = "/login")
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

   @GetMapping(value="/key/{token}")
   public ResponseEntity<Resource> getKeyByToken(@PathVariable String token) throws Exception {
      Resource file = userService.getKeyByToken(token);

      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + file.getFilename() + "\"").body(file);
   }

    @GetMapping("/publickey/{userId}")
   public String getPublicKeyByUserId(@PathVariable Long userId) {
      return userService.getPublicKeyByToken(userId);
   }
}
