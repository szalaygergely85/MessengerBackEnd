package com.gege.ideas.messenger.pages;

import com.gege.ideas.messenger.tokens.Token;
import com.gege.ideas.messenger.tokens.TokenService;
import com.gege.ideas.messenger.tokens.TokenType;
import com.gege.ideas.messenger.user.entity.User;
import com.gege.ideas.messenger.user.service.UserService;
import com.gege.ideas.messenger.utils.HashUtil;
import java.time.Duration;
import java.time.Instant;
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
      @PathVariable("token") String tokenString,
      Model model
   ) {
      Token token = tokenService.getTokenByToken(tokenString);
      if (token != null) {
         if (
            token.isActive() &&
            Duration
                  .between(
                     Instant.ofEpochMilli(token.getTimestamp()),
                     Instant.now()
                  )
                  .toHours() <
               1
         ) {
            model.addAttribute("token", tokenString);
            return "forgot_password";
         }
      }
      model.addAttribute("error", "Invalid Token");
      return "error";
   }

   //TODO important for google play
   @GetMapping("/request-account-deletion")
   public String requestAccountDeletion() {
      return "request_delete";
   }

   @PostMapping("/request-account-deletion")
   public String requestAccountDeletionPost(
      @RequestParam String username,
      @RequestParam String password,
      Model model
   ) {
      try {
         // Authenticate user
         User user = userService.logInUser(username, password);

         if (user == null) {
            model.addAttribute(
               "error",
               "Invalid email or password. Please try again."
            );
            return "delete-request-result";
         }

         // Send deletion email
         userService.sendDeleteAccountEmail(username);

         model.addAttribute(
            "message",
            "We've sent a confirmation email to " +
            username +
            ". Please click the link in the email within 5 minutes to confirm account deletion."
         );
         return "delete-request-result";
      } catch (Exception e) {
         model.addAttribute(
            "error",
            "An error occurred while processing your request. Please try again later."
         );
         return "delete-request-result";
      }
   }

   @GetMapping("/delete-account/{token}")
   public String deleteAccount(
      @PathVariable("token") String tokenString,
      Model model
   ) {
      try {
         Token token = tokenService.getTokenByToken(tokenString);

         // Validate token exists
         if (token == null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "account-deleted";
         }

         // Validate token type
         if (token.getTokenType() != TokenType.DELETE_ACCOUNT) {
            model.addAttribute("error", "Invalid token type.");
            return "account-deleted";
         }

         // Validate token is active and not expired (5 minutes = 5 * 60 * 1000 milliseconds)
         if (
            !token.isActive() ||
            Duration
                  .between(
                     Instant.ofEpochMilli(token.getTimestamp()),
                     Instant.now()
                  )
                  .toMinutes() >=
               5
         ) {
            model.addAttribute(
               "error",
               "Token has expired. The deletion link is only valid for 5 minutes. Please submit a new deletion request."
            );
            return "account-deleted";
         }

         // Delete the user
         Long userId = token.getUserId();
         userService.deleteUser(userId);

         // Deactivate the token
         token.setActive(false);
         tokenService.saveToken(token);

         model.addAttribute(
            "message",
            "Your account has been successfully deleted. All your data has been permanently removed."
         );
         return "account-deleted";
      } catch (Exception e) {
         model.addAttribute(
            "error",
            "An error occurred while deleting your account. Please try again or contact support."
         );
         return "account-deleted";
      }
   }

   @PostMapping("/changePassword")
   public String changePassword(
      @RequestParam String newPassword,
      @RequestParam String confirmPassword,
      @RequestParam String tokenString,
      Model model
   ) {
      try {
         Token token = tokenService.getTokenByToken(tokenString);

         // Validate token exists
         if (token == null) {
            model.addAttribute(
               "error",
               "Invalid or expired token. Please request a new password reset link."
            );
            return "changePasswordResult";
         }

         // Validate token is active and not expired
         if (
            !token.isActive() ||
            Duration
                  .between(
                     Instant.ofEpochMilli(token.getTimestamp()),
                     Instant.now()
                  )
                  .toHours() >=
               1
         ) {
            model.addAttribute(
               "error",
               "Token has expired. Please request a new password reset link."
            );
            return "changePasswordResult";
         }

         // Change password
         userService.changePassword(
            token.getUserId(),
            HashUtil.hashPassword(newPassword)
         );

         // Deactivate token after successful password change
         token.setActive(false);
         tokenService.saveToken(token);

         model.addAttribute(
            "message",
            "Your password has been changed successfully."
         );
         return "changePasswordResult";
      } catch (Exception e) {
         model.addAttribute(
            "error",
            "An error occurred while changing your password. Please try again."
         );
         return "changePasswordResult";
      }
   }
}
