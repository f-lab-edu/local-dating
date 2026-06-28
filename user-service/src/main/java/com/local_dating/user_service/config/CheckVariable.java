package com.local_dating.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CheckVariable implements CommandLineRunner {

    @Value("${AWS_SERVER}")
    private String awsServer;

    @Value("${spring.security.oauth2.client.registration.google.client-id:NOT_FOUND}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret:NOT_FOUND}")
    private String googleClientSecret;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("AWS Server: " + awsServer);
        System.out.println("Google Client ID: " + googleClientId);
        System.out.println("Google Client Secret: " + googleClientSecret);
    }
}
