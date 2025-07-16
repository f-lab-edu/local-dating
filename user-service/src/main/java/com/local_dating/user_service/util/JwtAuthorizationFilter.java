package com.local_dating.user_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.CustomUserDetails;
import com.local_dating.user_service.application.CustomUserDetailsService;
import com.local_dating.user_service.util.exception.BusinessException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthorizationFilter(final JwtUtil jwtUtil, final ObjectMapper mapper, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request
            , final HttpServletResponse response
            , final FilterChain filterChain) throws ServletException, IOException {

        final String path = request.getRequestURI();
        if (path.equals("/v1/users/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

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
            //if (claims != null & jwtUtil.validateClaims(claims)) {
                final String userId = claims.getSubject();
                logger.debug("userId: " + userId);
                //String email = claims.getSubject();
                //System.out.println("email : "+email);

                final CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userId);
                final Authentication authentication =
                        new UsernamePasswordAuthenticationToken(claims.get("no"), "", userDetails.getAuthorities());
                        //new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                        //new UsernamePasswordAuthenticationToken(userId, "", new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException ex) {
            // ① “토큰 만료” 예외를 던져서 AuthenticationEntryPoint 로 위임
            logger.info(ex.getMessage());
            throw new BusinessException(MessageCode.INSUFFICIENT_AUTHENTICATION);
            //throw new InsufficientAuthenticationException("Access token expired", ex);
        } catch (JwtException | IllegalArgumentException ex) {
            // ② 시그니처 불일치 등은 BadCredentialsException 으로 던져서 401 처리
            logger.info(ex.getMessage());
            throw new BusinessException(MessageCode.BAD_CREDENTIAL_EXCEPTION);
            //throw new BadCredentialsException("Invalid access token", ex);
        } catch (Exception e) {
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);

        }
        filterChain.doFilter(request, response);
    }
}
