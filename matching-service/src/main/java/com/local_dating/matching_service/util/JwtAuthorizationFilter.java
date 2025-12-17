package com.local_dating.matching_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    public JwtAuthorizationFilter(final JwtUtil jwtUtil, final ObjectMapper mapper) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final Map<String, Object> errorDetails = new HashMap<>();

        try {
            final String accessToken = jwtUtil.resolveToken(request);
            if (StringUtils.isEmpty(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            logger.debug("token : " + accessToken);
            final Claims claims = jwtUtil.resolveClaims(request);

            if (jwtUtil.validateClaims(claims)) {
                final String userNo = claims.getSubject();
                logger.debug("userNo: " + userNo);

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (claims.get("role") != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString()));
                }

                //final CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userId);
                final Authentication authentication = new UsernamePasswordAuthenticationToken(userNo, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            // ① “토큰 만료” 예외를 던져서 AuthenticationEntryPoint 로 위임
            logger.info(ex.getMessage(), ex);
            throw new InsufficientAuthenticationException(MessageCode.INSUFFICIENT_AUTHENTICATION.getMessage(), ex);
        } catch (JwtException | IllegalArgumentException ex) {
            // ② 시그니처 불일치 등은 BadCredentialsException 으로 던져서 401 처리
            logger.info(ex.getMessage(), ex);
            throw new BadCredentialsException(MessageCode.BAD_CREDENTIAL_EXCEPTION.getMessage(), ex);
        } catch (Exception e) {
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
