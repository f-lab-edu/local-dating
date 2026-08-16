package com.local_dating.user_service.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CiGeneratorUtil {

    private static final String SECRET = "ci-secret";
    //private static final String SECRET = "ci-secret";

    public String generateCi(long userKey) {
    //public static String generateCi(long userKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String source = SECRET + ":" + userKey;

            byte[] hash = md.digest(source.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
