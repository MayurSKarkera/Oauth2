package com.microservices.server.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.util.Base64;

import java.security.PrivateKey;
import org.springframework.beans.factory.annotation.Autowired;


public class AESUtil {

    //  Decrypt AES key using RSA (server side)
	public static SecretKey decryptAESKey(String encryptedKey, PrivateKey privateKey) {
	    try {
	        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);

	        byte[] decoded = Base64.getDecoder().decode(encryptedKey);
	        byte[] keyBytes = cipher.doFinal(decoded);

	        return new SecretKeySpec(keyBytes, "AES");

	    } catch (Exception e) {
	        throw new RuntimeException("Error decrypting AES key", e);
	    }
	}

    //  Encrypt data using dynamic AES key
    public static String encrypt(String data, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    //  Decrypt data using dynamic AES key
    public static String decrypt(String encryptedData, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}