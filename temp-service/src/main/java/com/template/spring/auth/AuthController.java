package com.template.spring.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@ConditionalOnProperty(name = "app.auth.mode", havingValue = "self")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder encoder;
    private final String issuer;
    private final String audience;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder encoder,
                          @Value("${app.auth.issuer}") String issuer,
                          @Value("${app.auth.audience}") String audience) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.issuer = issuer;
        this.audience = audience;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        // 인증 성공 후 서버가 부여한 권한으로 15분짜리 Access Token을 발급한다.
        if (request.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        try {
            var auth = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
            Instant now = Instant.now();
            // sub는 예제 계정명이다. 회원 DB를 도입하면 변경되지 않는 회원 ID를 사용한다.
            var claims = JwtClaimsSet.builder().issuer(issuer).subject(auth.getName())
                    .audience(List.of(audience)).issuedAt(now).expiresAt(now.plusSeconds(900))
                    .claim("roles", List.of("USER")).claim("scope", "notes.read notes.write").build();
            return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                    .body(new TokenResponse(encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), "Bearer", 900));
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    public record LoginRequest(@NotBlank @Size(max = 100) String username,
                               @NotBlank @Size(max = 72) String password) {
        @Override public String toString() { return "LoginRequest[REDACTED]"; }
    }
    public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
        @Override public String toString() { return "TokenResponse[REDACTED]"; }
    }
}
