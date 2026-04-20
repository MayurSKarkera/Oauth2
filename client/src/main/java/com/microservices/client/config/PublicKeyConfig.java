package com.microservices.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class PublicKeyConfig {

    @Bean
    public PublicKey publicKey() {
        try {
            //  PASTE YOUR SERVER PUBLIC KEY HERE (Base64 string)
            String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0iNEHtbORm0nbJmgl8Jx/aQZfTuNFjbmC3kOfWFmPD4nzluBnzKtTRD2rFRZTn5pKhktI/KDYZsvbY1ER9den+xkQJtzNeyXmjTtw9I3OZ2dd3yvN4QSvqxV92HDuezGNXZS1y/sMl/jLatrLVI7zCWKYZP8Ptpw+LNWcW6flm7qP0sN9VFZqkhqQtBRdYXjkubkueS/r/CJZXgneEQDCn96AC4JmDwFhoZbueR6DUuaViS65txRdv+rDoiVyN3/IYuahWqT/ggelnMIWHMEQDSOcV01lDJHJNaY8ruIWubMLhDFoogLZ4XyWU/OnvtOoERvIezYk4vJ6qqLYSnKlwIDAQAB";
            
            System.out.println("KEY LENGTH = " + key.length());
            // Decode Base64 key
            byte[] decoded = Base64.getDecoder().decode(key);

            // Convert to PublicKey object
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }
}