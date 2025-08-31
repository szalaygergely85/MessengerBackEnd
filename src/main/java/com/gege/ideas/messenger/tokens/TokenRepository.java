package com.gege.ideas.messenger.tokens;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
   Token getTokenByUserId(long userId);
   Token getTokensByToken(String token);
}
