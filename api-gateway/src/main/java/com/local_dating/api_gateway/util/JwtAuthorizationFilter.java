package com.local_dating.api_gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter implements GatewayFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        try{
            List<String> authorizations = getAuthorizations(exchange);

            if(isNotExistsAuthorizationHeader(authorizations)) {
                throw new NotExistsAuthorization();
            }

            String authorization = authorizations.stream()
                    .filter(this::isBearerType)
                    .findFirst()
                    .orElseThrow(NotExistsAuthorization::new);

            String jwtToken = parseAuthorizationToken(authorization);
            if(isValidateExpire(jwtToken)) {
                throw new AccessTokenExpiredException();
            }

            exchange.getRequest().mutate().header(X_GATEWAY_HEADER, getSubjectOf(jwtToken));
            return chain.filter(exchange);
        } catch(NotExistsAuthorization e1) {
            return sendErrorResponse(exchange, 701, e1);
        } catch(AccessTokenExpiredException e2) {
            return sendErrorResponse(exchange, 702, e2);
        } catch(Exception e3){
            return sendErrorResponse(exchange, 999, e3);
        }

        private Mono<Void> sendErrorResponse(ServerWebExchange exchange, int errorCode, Exception e) {
            try {
                ErrorResponse errorResponse = new ErrorResponse(errorCode, e.getMessage());
                String errorBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);

                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                DataBuffer buffer = response.bufferFactory().wrap(errorBody.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Flux.just(buffer));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }

        private boolean isBearerType(String authorization) {
            return authorization.startsWith(AUTH_TYPE);
        }

        private List<String> getAuthorizations(ServerWebExchange exchange) {
            ServerHttpRequest request = exchange.getRequest();
            return request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        }

        private String parseAuthorizationToken(String authorization) {
            return authorization.replace(AUTH_TYPE, "").trim();
        }

        private boolean isNotExistsAuthorizationHeader(List<String> authorizations) {
            return authorizations == null || authorizations.isEmpty();
        }

        private String getSubjectOf(String jwtToken) {
            return Jwts.parser().verifyWith(secretKey())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload()
                    .getSubject();
        }

        private boolean isValidateExpire(String jwtToken) {
            Date expiration = Jwts.parser().verifyWith(secretKey())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        }

        private SecretKey secretKey() {
            return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        }

        record ErrorResponse(int code, String message){}

        //return null;
    }
}
/*
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
*/
