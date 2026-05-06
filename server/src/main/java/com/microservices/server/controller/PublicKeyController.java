package com.microservices.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Base64;

@RestController
public class PublicKeyController {

    @Autowired
    private PublicKey publicKey;

    @GetMapping("/public-key")
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}