package com.gege.ideas.messenger.tokens;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

   private final TokenRepository tokenRepository;

   @Autowired
   public TokenService(TokenRepository tokenRepository) {
      this.tokenRepository = tokenRepository;
   }

   public Token getActiveTokenByUser(long userId) {
      return tokenRepository.getTokenByUserIdAndActiveTrue(userId);
   }

   public void setTokenInactive(Token token) {
      token.setActive(false);
      tokenRepository.save(token);
   }

   public Token getTokenByToken(String token) {
      return tokenRepository.getTokensByToken(token);
   }

   public Token generateToken(long userId) {
      Token token = getActiveTokenByUser(userId);

      if (token == null) {
         token =
         tokenRepository.save(
            new Token(
               userId,
               TokenType.FORGOT_PASSWORD,
               UUID.randomUUID().toString(),
               System.currentTimeMillis(),
               true
            )
         );
      }
      return token;
   }
}
