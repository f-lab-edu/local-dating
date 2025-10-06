package com.local_dating.user_service.util;

import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.util.exception.InvalidateClaimsException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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

    public String createAccessToken(final UserVO user) {
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(user.loginId())
                .claim("no", user.no()) // user테이블 id
                .claim("name",user.name())
                .claim("role", "USER")
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(final UserVO user) {
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setSubject(user.loginId())
                .claim("no", user.no()) // user테이블 id
                .claim("name",user.name())
                .claim("role", "USER")
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

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
        if (authentication != null && authentication.startsWith(TOKEN_PREFIX)) {
            return authentication.substring(TOKEN_PREFIX.length());
        }
        return authentication;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        if (isNull(claims)) {
            throw new InvalidateClaimsException(MessageCode.INVALIDATE_CLAIMS_EXCEPTION.getMessage());
        }
        return claims.getExpiration().after(new Date());
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
            String userId = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(userId, token, Collections.emptyList());
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }

}
