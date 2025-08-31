package com.gege.ideas.messenger.tokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

   private final TokenRepository tokenRepository;

   @Autowired
   public TokenService(TokenRepository tokenRepository) {
      this.tokenRepository = tokenRepository;
   }

   public Token getTokenByUser(long userId) {
      return tokenRepository.getTokenByUserId(userId);
   }

   public Token getTokenByToken(String token) {
      return tokenRepository.getTokensByToken(token);
   }
}
