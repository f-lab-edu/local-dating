package com.local_dating.matching_service.util;

import com.local_dating.matching_service.application.AuthenticatedUserDetails;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;

public final class AccessTokenAuthentication {

    private AccessTokenAuthentication() {}

    public static Authentication verifiedClaims(Claims claims) {
            if (claims == null || !"ACCESS".equals(claims.get("tokenType", String.class))
                    || claims.getExpiration() == null || !claims.getExpiration().after(new Date())) {
                throw new BadCredentialsException(MessageCode.INVALIDATE_CLAIMS_EXCEPTION.getMessage());
            }
            AuthenticatedUserDetails principal = new AuthenticatedUserDetails(Long.valueOf(claims.getSubject()));
            String role = claims.get("role", String.class);
            /*if (role == null || role.isBlank() || role.startsWith("ROLE_")) {
                throw new BadCredentialsException("Invalid access token role");
            }*/
            return new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }
}
