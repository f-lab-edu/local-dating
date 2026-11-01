package com.local_dating.matching_service.util;

import com.local_dating.matching_service.util.exception.InvalidateClaimsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretString;

    @Value("${jwt.access-token-validity-seconds}")
    private long accessTokenValidity;

    @Value("${jwt.refresh-token-validity-seconds}")
    private long refreshTokenValidity;

    /*private final SecretKey secret_key = Keys.hmacShaKeyFor("thisisaverysecretkeyandsecure123".getBytes(StandardCharsets.UTF_8));
    private long accessTokenValidity = 60 * 60 * 1000;
    private long refreshTokenValidity = 24 * 60 * 60 * 1000;*/

    private SecretKey secretKey;
    private JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String REFRESH_HEADER = "Refresh-Token";
    private final String TOKEN_PREFIX = "Bearer ";

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser().setSigningKey(secretKey).build();
    }
    /*public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key).build();
    }*/

    public Claims parseJwtClaims(final String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(final HttpServletRequest req) {
        try {
            final String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(final HttpServletRequest request) {

        final String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String resolveRefreshToken(String authentication) {
        //public String resolveRefreshToken(HttpServletRequest request) {
        if (authentication != null && authentication.startsWith(TOKEN_PREFIX)) {
            return authentication.substring(TOKEN_PREFIX.length());
        }
        return authentication;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        if (isNull(claims)) {
            throw new InvalidateClaimsException(MessageCode.INVALIDATE_CLAIMS_EXCEPTION.getMessage());
        }
        if (!"ACCESS".equals(claims.get("tokenType"))) {
            throw new BadCredentialsException(MessageCode.INVALIDATE_CLAIMS_EXCEPTION.getMessage());
        }
        return claims.getExpiration() != null && claims.getExpiration().after(new Date());
    }

    public String getUserId(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    // JWT 토큰을 기반으로 Authentication 객체 생성
    public Authentication getAuthenticationFromToken(String token) {
        try {
            Claims claims = parseJwtClaims(token.replace(TOKEN_PREFIX, "")); // Bearer 제거 후 파싱
            return AccessTokenAuthentication.verifiedClaims(claims);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT Token", e);
        }
    }

}
