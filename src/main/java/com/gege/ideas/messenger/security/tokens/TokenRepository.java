package com.gege.ideas.messenger.security.tokens;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
   Token getTokenByUserIdAndActiveTrue(long userId);
   Token getTokensByToken(String token);
}
