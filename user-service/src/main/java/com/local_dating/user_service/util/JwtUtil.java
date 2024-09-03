package com.local_dating.user_service.util;

import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.util.exception.InvalidateClaimsException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

@Component
public class JwtUtil {
    private final SecretKey secret_key = Keys.hmacShaKeyFor("thisisaverysecretkeyandsecure123".getBytes(StandardCharsets.UTF_8));
    //private final String secret_key = "mysecretkey";
    private long accessTokenValidity = 60*60*1000;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(secret_key).build();
    }

    public String createToken(final UserVO user) {
        /*Claims claims = (Claims) Jwts.claims().setSubject(user.userId());
        claims.put("pwd",user.pwd());
        claims.put("name",user.name());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();*/

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setSubject(user.userId())
                //.claim("userId",user.userId())
                //.claim("pwd",user.pwd())
                .claim("name",user.name())
                //.setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(final String token) {
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

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        //claims = null; //테스트
        if (!isNull(claims)) {
            return claims.getExpiration().after(new Date());
        } else {
            throw new InvalidateClaimsException(MessageCode.INVALIDATE_CLAIMS_EXCEPTION.getMessage());
        }
        /*try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }*/
    }

    public String getUserId(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}