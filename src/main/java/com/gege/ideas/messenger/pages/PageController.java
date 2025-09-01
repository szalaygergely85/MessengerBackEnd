package com.gege.ideas.messenger.pages;

import com.gege.ideas.messenger.tokens.Token;
import com.gege.ideas.messenger.tokens.TokenService;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

   private final TokenService tokenService;
   private final UserService userService;

   @Autowired
   public PageController(TokenService tokenService, UserService userService) {
      this.tokenService = tokenService;
      this.userService = userService;
   }

   @GetMapping("/forgot-password/{token}")
   public String changePassword(
      @PathVariable("token") String token,
      Model model
   ) {
      model.addAttribute("token", token);
      return "forgot_password";
   }

   @PostMapping("/changePassword")
   public String changePassword(
      @RequestParam String newPassword,
      @RequestParam String tokenString,
      Model model
   ) {
      Token token = tokenService.getTokenByToken(tokenString);
      userService.changePassword(token.getUserId(), HashUtil.hashPassword(newPassword));

      return "changePasswordResult"; // JSP to show after success/fail
   }
}
