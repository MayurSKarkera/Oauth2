package com.microservices.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import com.microservices.client.model.Employee;
import com.microservices.client.util.AESUtil;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private PublicKey publicKey;

    public Employee callServer() {

        try {
            RestTemplate restTemplate = new RestTemplate();

            // 🔐 STEP 1: Get OAuth2 token
            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setBasicAuth("my-client", "secret");
            tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
            tokenBody.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> tokenRequest =
                    new HttpEntity<>(tokenBody, tokenHeaders);

            Map tokenResponse = restTemplate.postForObject(
                    "http://localhost:9000/oauth2/token",
                    tokenRequest,
                    Map.class
            );

            // ✅ Check token
            if (tokenResponse == null || tokenResponse.get("access_token") == null) {
                throw new RuntimeException("Failed to get access token");
            }

            String token = (String) tokenResponse.get("access_token");

            // 🔐 STEP 2: Generate AES key
            SecretKey aesKey = AESUtil.generateAESKey();

            // 🧾 STEP 3: Plain data
            String plainData = "101";
            System.out.println("PLAIN REQUEST = " + plainData);

            // 🔐 STEP 4: Encrypt data
            String encryptedData = AESUtil.encrypt(plainData, aesKey);

            // 🔐 STEP 5: Encrypt AES key using RSA
            String encryptedKey = AESUtil.encryptAESKey(aesKey, publicKey);

            System.out.println("ENCRYPTED DATA = " + encryptedData);
            System.out.println("ENCRYPTED AES KEY = " + encryptedKey);

            // 📦 STEP 6: Create JSON body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("data", encryptedData);
            requestBody.put("key", encryptedKey);

            // 🔐 STEP 7: Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token); // cleaner way
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(requestBody, headers);

            // 📡 STEP 8: Call server
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/api/employee",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String encryptedResponse = response.getBody();

            // ✅ Check response
            if (encryptedResponse == null) {
                throw new RuntimeException("Empty response from server");
            }

            System.out.println("ENCRYPTED RESPONSE = " + encryptedResponse);

            // 🔓 STEP 9: Decrypt response
            String decryptedResponse = AESUtil.decrypt(encryptedResponse, aesKey);
            System.out.println("DECRYPTED RESPONSE = " + decryptedResponse);

            // 🧾 STEP 10: Convert to Employee
            Employee emp = new Employee();
            emp.setId("101");
            emp.setName(decryptedResponse);

            return emp;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in client call", e);
        }
    }
}