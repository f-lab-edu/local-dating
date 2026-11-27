package com.template.spring.config;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration(proxyBeanMethods = false)
public class JwtConfig {
    // 외부 인증 모드에서는 공개 JWKS만 사용하며 개인 키를 보유하지 않는다.
    @Bean
    @ConditionalOnProperty(name = "app.auth.mode", havingValue = "external", matchIfMissing = true)
    JwtDecoder externalJwtDecoder(@Value("${app.auth.jwk-set-uri}") String jwks,
                                  @Value("${app.auth.issuer}") String issuer,
                                  @Value("${app.auth.audience}") String audience) {
        if (!jwks.startsWith("https://")) throw new IllegalArgumentException("External JWKS must use HTTPS");
        return validate(NimbusJwtDecoder.withJwkSetUri(jwks).build(), issuer, audience);
    }

    // 서명뿐 아니라 만료 시각, 발급자, 대상 서비스 및 사용자 식별자를 검증한다.
    public static JwtDecoder validate(NimbusJwtDecoder decoder, String issuer, String audience) {
        if (issuer.isBlank() || audience.isBlank()) throw new IllegalArgumentException("JWT issuer and audience are required");
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuer),
                jwt -> jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(Instant.now())
                        && jwt.getAudience() != null && jwt.getAudience().contains(audience)
                        && jwt.getSubject() != null && !jwt.getSubject().isBlank()
                        && (jwt.hasClaim("scope") || jwt.hasClaim("scp"))
                        ? OAuth2TokenValidatorResult.success()
                        : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token"))));
        return decoder;
    }
}
