package com.microservices.client.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import com.microservices.client.model.Employee;
import com.microservices.client.util.AESUtil;

import javax.crypto.SecretKey;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {

    public Employee callServer() {

        try {
            RestTemplate restTemplate = new RestTemplate();

            // STEP 1: Fetch server public key
            String publicKeyString = restTemplate.getForObject(
                    "http://localhost:8081/public-key",
                    String.class
            );

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey =
                    keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));

            System.out.println("SERVER PUBLIC KEY = " + publicKeyString);

            // STEP 2: Get OAuth2 token
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

            if (tokenResponse == null || tokenResponse.get("access_token") == null) {
                throw new RuntimeException("Failed to get access token");
            }

            String token = (String) tokenResponse.get("access_token");

            // STEP 3: Generate AES key
            SecretKey aesKey = AESUtil.generateAESKey();

            // STEP 4: Plain request data
            String plainData = "101";
            System.out.println("PLAIN REQUEST = " + plainData);

            // STEP 5: Encrypt data using AES key
            String encryptedData = AESUtil.encrypt(plainData, aesKey);

            // STEP 6: Encrypt AES key using server public key
            String encryptedKey = AESUtil.encryptAESKey(aesKey, publicKey);

            System.out.println("ENCRYPTED DATA = " + encryptedData);
            System.out.println("ENCRYPTED AES KEY = " + encryptedKey);

            // STEP 7: Create JSON body containing encrypted data and encrypted AES key
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("data", encryptedData);
            requestBody.put("key", encryptedKey);

            // STEP 8: Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(requestBody, headers);

            // STEP 9: Call Resource Server
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/api/employee",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String encryptedResponse = response.getBody();

            if (encryptedResponse == null) {
                throw new RuntimeException("Empty response from server");
            }

            System.out.println("ENCRYPTED RESPONSE = " + encryptedResponse);

            // STEP 10: Decrypt response using same AES key
            String decryptedResponse = AESUtil.decrypt(encryptedResponse, aesKey);
            System.out.println("DECRYPTED RESPONSE = " + decryptedResponse);

            // STEP 11: Convert response to Employee object
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