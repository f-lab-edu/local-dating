package com.local_dating.user_service.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.time.Duration;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final StringRedisTemplate stringRedisTemplate;
    private final String key = "oauth2_authorization_request:";
    private static final Duration TTL = Duration.ofMinutes(3);

    /***
     * OAuth 콜백 시 호출, state 꺼내기
     * @param request the {@code HttpServletRequest}
     * @return
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");

        if (state == null || state.isBlank()) {
            return null;
        }

        String value = stringRedisTemplate.opsForValue().get(key + state);

        if (value == null) {
            return null;
        }

        byte[] bytes = Base64.getUrlDecoder().decode(value);
        return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(bytes);
    }

    /***
     * OAuth 최초 요청 시 호출 (/oauth2/authorization/google)
     * @param authorizationRequest the {@link OAuth2AuthorizationRequest}
     * @param request the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        String state = authorizationRequest.getState();

        byte[] bytes = SerializationUtils.serialize(authorizationRequest);
        String value = Base64.getUrlEncoder().encodeToString(bytes);

        stringRedisTemplate.opsForValue().set(key + state, value, TTL);
    }

    /***
     * OAuth 콜백 시 호출, state 제거
     * @param request the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @return
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);

        String state = request.getParameter("state");
        if (state != null && !state.isBlank()) {
            stringRedisTemplate.delete(key + state);
        }

        return authorizationRequest;
    }
}
