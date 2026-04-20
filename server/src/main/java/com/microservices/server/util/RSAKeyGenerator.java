package com.microservices.server.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyGenerator {

    public static void main(String[] args) throws Exception {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair pair = keyGen.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        System.out.println("PUBLIC KEY = " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("PRIVATE KEY = " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }
}