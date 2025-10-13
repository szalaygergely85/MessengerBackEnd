package com.gege.ideas.messenger.security.key.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Deprecated
public class KeyStoreUtil {

   private static final String RSA_ALGORITHM = "RSA";
   private static final String AES_ALGORITHM = "AES";
   private static final int AES_KEY_SIZE = 256;

   public static String generateAndStoreKeys(
      String privateKeyPath,
      String aesKeyPath
   ) throws Exception {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
         RSA_ALGORITHM
      );
      keyPairGenerator.initialize(2048);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      SecretKey secretKey = _generateAESKey();

      byte[] encryptedPrivateKey = _encryptKey(
         keyPair.getPrivate().getEncoded(),
         secretKey
      );

      _storeKey(encryptedPrivateKey, privateKeyPath);
      _storeKey(secretKey.getEncoded(), aesKeyPath);

      return Base64
         .getEncoder()
         .encodeToString(keyPair.getPublic().getEncoded());
   }

   private static SecretKey _generateAESKey() throws NoSuchAlgorithmException {
      KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
      keyGenerator.init(AES_KEY_SIZE);
      return keyGenerator.generateKey();
   }

   private static byte[] _encryptKey(byte[] keyData, SecretKey secretKey)
      throws Exception {
      Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return cipher.doFinal(keyData);
   }

   private static void _storeKey(byte[] keyData, String filePath)
      throws IOException {
      try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
         fos.write(Base64.getEncoder().encode(keyData));
      }
   }

   public static void main(String[] args) throws Exception {}
}
