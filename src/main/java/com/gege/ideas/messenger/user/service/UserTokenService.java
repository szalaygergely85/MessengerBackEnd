package com.gege.ideas.messenger.user.service;

import com.gege.ideas.messenger.user.entity.UserToken;
import com.gege.ideas.messenger.user.repository.UserTokenRepository;
import com.gege.ideas.messenger.utils.TokenGeneratorUtil;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {

   private final UserTokenRepository userTokenRepository;

   @Autowired
   public UserTokenService(UserTokenRepository userTokenRepository) {
      this.userTokenRepository = userTokenRepository;
   }

   public UserToken createUserToken() {
      UserToken userToken = new UserToken();

      userToken.setToken(TokenGeneratorUtil.generateNewToken());

      Date dateNow = new Date();

      userToken.setGenerationDate(dateNow);

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateNow);
      calendar.add(Calendar.WEEK_OF_YEAR, 2);
      Date dateExpired = calendar.getTime();

      userToken.setExpirationDate(dateExpired);

      return userTokenRepository.save(userToken);
   }

   public UserToken updateUserTokenWithUserId(
      UserToken userToken,
      Long userId
   ) {
      userToken.setUserId(userId);

      return userTokenRepository.save(userToken);
   }

   public UserToken getUserTokenByToken(String token) {
      return userTokenRepository.findByToken(token);
   }

   public UserToken getUserTokenByTokenId(Long id) {
      return userTokenRepository.findByUserTokenId(id);
   }
}
