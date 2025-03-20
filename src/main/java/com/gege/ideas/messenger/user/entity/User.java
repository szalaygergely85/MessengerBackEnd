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
   private String email;

   @Column(nullable = false)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password;

   @Lob
   @Column(columnDefinition = "LONGTEXT")
   private String publicKey;

   @Column
   String profilePictureUuid;

   @Column
   String status;

   @Column
   String tags;

   @Column
   private String token;

   public String getProfilePictureUuid() {
      return profilePictureUuid;
   }

   public void setProfilePictureUuid(String profilePictureUuid) {
      this.profilePictureUuid = profilePictureUuid;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getTags() {
      return tags;
   }

   public void setTags(String tags) {
      this.tags = tags;
   }

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


   public String getPublicKey() {
      return publicKey;
   }

   public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public User() {}

   public User(
      String displayName,
      String email,
      String password
   ) {
      this.displayName = displayName;
      this.email = email;
      this.password = password;
   }

   public User(
      Long userId,
      String displayName,
      String email,
      String password

   ) {
      this.userId = userId;
      this.displayName = displayName;
      this.email = email;
      this.password = password;

   }
}
