package com.gege.ideas.messenger.key.util;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyLoadUtil {

   private static final String AES_ALGORITHM = "AES";
   private static final String RSA_ALGORITHM = "RSA";

   public static String loadKeys(String privateKeyPath, String aesKeyPath)
      throws Exception {
      byte[] aesKeyData = loadKey(aesKeyPath);
      SecretKey secretKey = new SecretKeySpec(aesKeyData, AES_ALGORITHM);

      byte[] encryptedPrivateKeyData = loadKey(privateKeyPath);

      byte[] privateKeyData = decryptKey(encryptedPrivateKeyData, secretKey);

      PrivateKey privateKey = KeyFactory
         .getInstance(RSA_ALGORITHM)
         .generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));

      return Base64.getEncoder().encodeToString(privateKey.getEncoded());
   }

   private static byte[] loadKey(String filePath) throws IOException {
      return Base64
         .getDecoder()
         .decode(Files.readAllBytes(new File(filePath).toPath()));
   }

   private static byte[] decryptKey(
      byte[] encryptedKeyData,
      SecretKey secretKey
   ) throws Exception {
      Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return cipher.doFinal(encryptedKeyData);
   }
}
