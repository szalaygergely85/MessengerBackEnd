package com.gege.ideas.messenger.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   @Column(nullable = false)
   private String displayName;

   @Column(nullable = false)
   private String fullName;

   @Column(nullable = false)
   private String email;

   @Column(nullable = false)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password;

   @Column(nullable = false)
   private Long userTokenId;

   @Column(nullable = false)
   private Long phoneNumber;

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long id) {
      this.userId = id;
   }

   public String getDisplayName() {
      return displayName;
   }

   public void setDisplayName(String surName) {
      this.displayName = surName;
   }

   public String getFullName() {
      return fullName;
   }

   public void setFullName(String firstName) {
      this.fullName = firstName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Long getUserTokenId() {
      return userTokenId;
   }

   public void setUserTokenId(Long userTokenId) {
      this.userTokenId = userTokenId;
   }

   public Long getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(Long phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public User() {}

   public User(String displayName, String fullName, String email, String password, Long userTokenId, Long phoneNumber) {
      this.displayName = displayName;
      this.fullName = fullName;
      this.email = email;
      this.password = password;
      this.userTokenId = userTokenId;
      this.phoneNumber = phoneNumber;
   }

   public User(
      Long userId,
      String displayName,
      String fullName,
      String email,
      String password,
      Long userTokenId,
      Long phoneNumber
   ) {
      this.userId = userId;
      this.displayName = displayName;
      this.fullName = fullName;
      this.email = email;
      this.password = password;
      this.userTokenId = userTokenId;
      this.phoneNumber = phoneNumber;
   }
}
