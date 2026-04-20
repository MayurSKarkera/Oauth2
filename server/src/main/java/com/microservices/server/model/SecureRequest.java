package com.microservices.server.model;

public class SecureRequest {

    private String data; // encrypted data
    private String key;  // encrypted AES key

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}