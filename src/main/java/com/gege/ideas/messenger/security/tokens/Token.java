package com.gege.ideas.messenger.security.tokens;

import jakarta.persistence.*;

@Entity
@Table(name = "token")
public class Token {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long tokenId;

   @Column(nullable = false)
   private Long userId;

   @Column(nullable = false)
   private TokenType tokenType;

   @Column(nullable = false)
   private String token;

   @Column(nullable = false)
   private Long timestamp;

   @Column(nullable = false)
   private boolean active;

   public Token(
      Long userId,
      TokenType tokenType,
      String token,
      long timestamp,
      boolean active
   ) {
      this.userId = userId;
      this.tokenType = tokenType;
      this.token = token;
      this.timestamp = timestamp;
      this.active = active;
   }

   public long getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

   public boolean isActive() {
      return active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public Token() {}

   public Long getTokenId() {
      return tokenId;
   }

   public void setTokenId(Long tokenId) {
      this.tokenId = tokenId;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public TokenType getTokenType() {
      return tokenType;
   }

   public void setTokenType(TokenType tokenType) {
      this.tokenType = tokenType;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }
}
