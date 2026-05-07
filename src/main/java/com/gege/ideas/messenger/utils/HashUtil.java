package com.gege.ideas.messenger.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashUtil {

   private static final BCryptPasswordEncoder BCRYPT =
      new BCryptPasswordEncoder();

   /**
    * BCrypt-hashes the given password string (typically a SHA-256 hex digest sent by the client).
    */
   public static String hashPassword(String password) {
      return BCRYPT.encode(password);
   }

   public static boolean verifyPassword(String rawPassword, String storedHash) {
      return BCRYPT.matches(rawPassword, storedHash);
   }

   /**
    * SHA-256 hex digest — used when we have a plaintext value (e.g. config-file passwords)
    * and need to produce the same bytes the client would send.
    */
   public static String sha256(String input) {
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] hashedBytes = md.digest(input.getBytes());
         StringBuilder sb = new StringBuilder();
         for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
         }
         return sb.toString();
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("SHA-256 not available", e);
      }
   }
}
