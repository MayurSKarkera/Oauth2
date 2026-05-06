package com.microservices.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class RSAKeyConfig {

    @Bean
    public KeyPair keyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    @Bean
    public PublicKey publicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    @Bean
    public PrivateKey privateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }
}