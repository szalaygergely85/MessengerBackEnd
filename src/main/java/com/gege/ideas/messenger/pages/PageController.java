package com.gege.ideas.messenger.pages;

import com.gege.ideas.messenger.tokens.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

   @Autowired
   private TokenService tokenService;

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
      @RequestParam String confirmPassword,
      @RequestParam String token,
      Model model
   ) {
      // 1. Validate passwords
      // 2. Change password in DB
      // 3. Return view name
      return "changePasswordResult"; // JSP to show after success/fail
   }
}
