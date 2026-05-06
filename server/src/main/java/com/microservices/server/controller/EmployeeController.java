package com.microservices.server.controller;

import java.security.PrivateKey;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.microservices.server.util.AESUtil;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    // RSA private key used for decrypting AES key
    @Autowired
    private PrivateKey privateKey;

    @PostMapping("/employee")
    public String getEmployee(@RequestBody Map<String, String> request) {

        try {

            // STEP 1: Read encrypted request values from client
            String encryptedData = request.get("data");
            String encryptedKey = request.get("key");

            // Display encrypted request values
            System.out.println("ENCRYPTED DATA (SERVER) = " + encryptedData);
            System.out.println("ENCRYPTED AES KEY (SERVER) = " + encryptedKey);

            // STEP 2: Decrypt AES key using RSA private key
            SecretKey aesKey =
                    AESUtil.decryptAESKey(encryptedKey, privateKey);

            // STEP 3: Decrypt actual request data using AES key
            String decryptedData =
                    AESUtil.decrypt(encryptedData, aesKey);

            // Display decrypted request
            System.out.println("DECRYPTED REQUEST (SERVER) = " + decryptedData);

            // STEP 4: Process request
            String response = "Received ID: " + decryptedData;

            // Display plain response before encryption
            System.out.println("PLAIN RESPONSE (SERVER) = " + response);

            // STEP 5: Encrypt response using same AES key
            String encryptedResponse =
                    AESUtil.encrypt(response, aesKey);

            // Display encrypted response
            System.out.println("ENCRYPTED RESPONSE (SERVER) = " + encryptedResponse);

            // STEP 6: Return encrypted response to client
            return encryptedResponse;

        } catch (Exception e) {

            // Prints actual error in console
            e.printStackTrace();

            // Throw custom runtime exception
            throw new RuntimeException("Server error", e);
        }
    }
}