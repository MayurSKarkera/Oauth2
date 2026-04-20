package com.microservices.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
public class PrivateKeyConfig {

    @Bean
    public PrivateKey privateKey() {
        try {
            String key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDM99CjRcpY05e4+JnIGjM0sTFeaCKW2Fd/varg/RjLEcibQlcMi7s4YKgWrK6j5bXs4T8J2bO/uSCSmIRQ/mOu5Pri3N2b8KICy7x1opsjGyJEWBOhz9W9Gtg5rRYBMyxbz5YMFAS0cGEaeoE6CAXRRwWf4y9Dx6bbPwowJ8TnuYdY/gLdKbLkTY8mj5kUt90O+dnBO9n53BE9SVHvQ5FrjgLsldowbAVKj4EU3Faxep0TVLLvRcBjkBZJQX1XiyudPeJSBrCLuBxz9Y6Bv+rcqgUVqyzKLowFa7e2fKjyYwH7sqRZVkT+iRADJnhUuSRxxRzYsS952uGpjpyO2kfHAgMBAAECggEAApzwLHyxyTMdXo7QyxPGblgJ9JWoRzsH1omQ+aBQZhpab9xHJtJLNztIOU3HRjCiApyWhKYhXALcrvHB0hSEKdEif6zoKFpHfnihdPB3QNnSNIPY8DmplGkYs5eIzcgcuyjo1UuWCcTbAA7qwdYjWNkqcbDgQ+FyN55Un2uKX5fgjiCMhChAjeQ33d5jgAOQj9J07gLzIEalYXtw/v5a3j+BEqrGkBzTza+KLferg9j6Tc/lUTffEpJloYeuGQlc3uJMAoi7FycWrXxYb6lmr4Y+z+DL/i4d2VPL94V1NWFAQ1d4n3RW0fgdjERLQeTaSZOVfCejfVzml7DYCz9c4QKBgQD2gNWjyep7v/SIy+Bd56y1xlgDQnKTs5RQIMQzt0OJQtUAs8/5qM3Sjpw5yAT1qN3s51k/PcKswNogvrBEqK/xgLgEhp2LJRlljiXJu3lkfPF1F8ct2P6cpBSSGC7xmsRKOPCOqhqioGzN4rYToGfTX9i4l9TXAjvZp1HxQcPAJwKBgQDU3VWs1qZolQVMMAQlJQ6nZR87njrBOfDXSU8ynU/kbnfz6+mAqOV2cgB+wcfCAUK2XUCBmY4Eo6OqfqzpaVCboBJihN08HEMm3YgYHTDRKL4gusFCNGjDlOy8HkACxGfT6+XKo3Kz5C5763rNvj31K7Nhup5BLh4zy96vQaBfYQKBgDu3yIoOj6z9OvutEKUPTEsZSxc3ENTxi3vCBGZW/piRRVMPPNJJ79sTy4tABtuQ1nrfLULh7ni1m+KU79UiXMWQHoSSLDaafUWaI1N1BJqwnjp00T8tM7m1Enq2OyEzEly84KJfWWaOoO1cACpPEPgnXzSRK/IOy3sWSwa8nPS/AoGAbHvAxi0+8d3ArWtrMWMJbNZkbIXfe8qDq7R44Fq79Lc3+uAoSKHLD+pZxkAMBhuOXFJ6JZKUhli/eIzGos5Bfo69z+fWuGPO9WfBjEiUMISLITMMeykqm0Kyw8zG4qImKCL5IaTbOEL6Tb1dAK4L9X8oFI5/SMiwK8Wi376adQECgYBbVs1WLxvjJYIHG1GhutM4pJOKAuNorFERCPfXsXQuCUDyYVXlmlkXNcQ6X9+b2E8sxfh2aYugskNvtry7HuXam+4mmuFENm2mexKdNwTsDLI4Yq021wmpan5+td1rBeFAc85JP3MHj7+PAqAjWSj34pXz5hq5kfyGEhmJU2MQ5w==";  //  paste here

            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }
}