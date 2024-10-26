package com.gege.ideas.messenger.user.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usertoken")
public class UserToken {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userTokenId;

   @Column
   private String token;

   @Column
   private Long userId;

   @Column
   private Date generationDate;

   @Column
   private Date expirationDate;

   public Long getUserTokenId() {
      return userTokenId;
   }

   public void setUserTokenId(Long userTokenId) {
      this.userTokenId = userTokenId;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userID) {
      this.userId = userID;
   }

   public Date getGenerationDate() {
      return generationDate;
   }

   public void setGenerationDate(Date generationDate) {
      this.generationDate = generationDate;
   }

   public Date getExpirationDate() {
      return expirationDate;
   }

   public void setExpirationDate(Date expirationDate) {
      this.expirationDate = expirationDate;
   }

   public UserToken(
      Long userTokenId,
      String token,
      Long userId,
      Date generationDate,
      Date expirationDate
   ) {
      this.userTokenId = userTokenId;
      this.token = token;
      this.userId = userId;
      this.generationDate = generationDate;
      this.expirationDate = expirationDate;
   }

   public UserToken() {}

   public void setTokenId(long l) {}
}
