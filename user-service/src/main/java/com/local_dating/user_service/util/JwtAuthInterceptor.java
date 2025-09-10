package com.local_dating.user_service.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = jwtUtil.resolveToken(request);

        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token missing");
            return false;
        }

        try {
            Claims claims = jwtUtil.parseJwtClaims(token);
            if (!jwtUtil.validateClaims(claims)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return false;
            }

            // 사용자 정보 request attribute에 저장
            request.setAttribute("id", claims.getSubject());
            return true;

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
    }
}
