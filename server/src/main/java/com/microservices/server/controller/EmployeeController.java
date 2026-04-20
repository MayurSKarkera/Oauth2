package com.microservices.server.controller;

import com.microservices.server.util.AESUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private PrivateKey privateKey;

    @PostMapping("/employee")
    public Map<String, String> getEmployee(@RequestBody Map<String, String> request) {

        try {
            String encryptedData = request.get("data");
            String encryptedKey = request.get("key");

            // Debug logs
            System.out.println("ENCRYPTED DATA (SERVER) = " + encryptedData);
            System.out.println("ENCRYPTED AES KEY (SERVER) = " + encryptedKey);
            
            //  Validate input
            if (encryptedData == null || encryptedKey == null) {
                throw new RuntimeException("Missing data or key");
            }

            //  STEP 1: Decrypt AES key using RSA
            System.out.println("STEP 1: Decrypting AES key...");
            SecretKey aesKey = AESUtil.decryptAESKey(encryptedKey, privateKey);
            System.out.println("STEP 2: AES key decrypted");
            
            //  STEP 2: Decrypt data using AES
            String decrypted = AESUtil.decrypt(encryptedData, aesKey);
            System.out.println("DECRYPTED REQUEST = " + decrypted);

            //  STEP 3: Process
            String response = "Received ID: " + decrypted;

            //  STEP 4: Encrypt response using same AES key
            String encryptedResponse = AESUtil.encrypt(response, aesKey);
            System.out.println("ENCRYPTED RESPONSE (SERVER) = " + encryptedResponse);

            //  Return JSON response
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("data", encryptedResponse);

            return responseMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error", e);
        }
    }
}