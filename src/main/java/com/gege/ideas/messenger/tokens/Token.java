package com.gege.ideas.messenger.tokens;

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

   public Token(Long userId, TokenType tokenType, String token) {
      this.userId = userId;
      this.tokenType = tokenType;
      this.token = token;
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
