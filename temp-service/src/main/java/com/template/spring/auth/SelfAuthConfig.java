package com.template.spring.auth;

import com.template.spring.config.JwtConfig;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

// 단독 서비스의 자체 인증 발급 기능. 외부 인증을 선택하면 생성되지 않는다.
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "app.auth.mode", havingValue = "self")
public class SelfAuthConfig {
    @Bean
    KeyPair signingKeys(@Value("${app.auth.private-key:}") String privateLocation,
                        @Value("${app.auth.public-key:}") String publicLocation,
                        @Value("${app.auth.ephemeral-key:false}") boolean ephemeral,
                        ResourceLoader loader, org.springframework.core.env.Environment environment) throws Exception {
        if (ephemeral) {
            // 개발/테스트 전용. 재시작하면 기존 토큰은 무효화된다.
            if (!environment.matchesProfiles("local | test") || environment.matchesProfiles("prod")) {
                throw new IllegalArgumentException("Ephemeral keys are local/test only");
            }
            var generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        }
        if (privateLocation.isBlank() || publicLocation.isBlank()) throw new IllegalArgumentException("RSA keys required");
        try (var privateStream = loader.getResource(privateLocation).getInputStream();
             var publicStream = loader.getResource(publicLocation).getInputStream()) {
            RSAPrivateKey privateKey = RsaKeyConverters.pkcs8().convert(privateStream);
            RSAPublicKey publicKey = RsaKeyConverters.x509().convert(publicStream);
            if (!privateKey.getModulus().equals(publicKey.getModulus()) || publicKey.getModulus().bitLength() < 2048) {
                throw new IllegalArgumentException("Invalid RSA key pair");
            }
            return new KeyPair(publicKey, privateKey);
        }
    }

    @Bean
    JwtEncoder jwtEncoder(KeyPair keys) {
        return NimbusJwtEncoder.withKeyPair((RSAPublicKey) keys.getPublic(), (RSAPrivateKey) keys.getPrivate()).build();
    }

    @Bean
    JwtDecoder selfJwtDecoder(KeyPair keys, @Value("${app.auth.issuer}") String issuer,
                             @Value("${app.auth.audience}") String audience) {
        return JwtConfig.validate(NimbusJwtDecoder.withPublicKey((RSAPublicKey) keys.getPublic()).build(), issuer, audience);
    }

    @Bean
    AuthenticationManager authenticationManager(@Value("${app.auth.username}") String username,
                                                @Value("${app.auth.password}") String password) {
        if (username.isBlank() || password.length() < 12 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("Valid account and BCrypt-compatible password required");
        }
        // 예제 계정이며 회원 DB를 사용하는 UserDetailsService로 교체할 수 있다.
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var users = new InMemoryUserDetailsManager(User.withUsername(username)
                .password(encoder.encode(password)).roles("USER").build());
        var provider = new DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
}
