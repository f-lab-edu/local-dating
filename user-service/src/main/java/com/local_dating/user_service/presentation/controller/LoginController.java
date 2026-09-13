package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.CustomUserDetails;
import com.local_dating.user_service.application.UserInterceptorLoginService;
import com.local_dating.user_service.application.UserLoginService;
import com.local_dating.user_service.application.UserOAuthService;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.vo.UserOAuthLinkVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.presentation.dto.UserOAuthDTO;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final UserInterceptorLoginService userInterceptorLoginService;
    private final UserOAuthService userOAuthService;

    @PostMapping(value = "/api/users/login")
    public LoginRes login(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {
        return userLoginService.login(userMapper.INSTANCE.toUserVO(userDTO), request);
    }

    @PostMapping(value = "/api/interceptor/users/login")
    public LoginRes loginInterceptor(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {
        return userInterceptorLoginService.login(userMapper.INSTANCE.toUserVO(userDTO), request);
    }

    @PostMapping("/api/users/{id}/refresh")
    public LoginRes refresh(
            @RequestHeader("Authorization") String authentication,
            HttpServletRequest request,
            final @PathVariable("id") long id
    ) {
        return userLoginService.refreshTokens(jwtUtil.resolveRefreshToken(authentication), request, id);
    }

    @GetMapping("/google")
    public Map<String, Object> google(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttributes();
    }

    @PostMapping("/api/users/{id}/linkOauth")
    public void linkOauth(@PathVariable("id") final Long id, @AuthenticationPrincipal final CustomUserDetails userDetails
            , @RequestBody @Valid final UserOAuthDTO userOAuthDTO
    ) {
        //userOAuthService.linkOauthByEmail(id, userOAuthDTO);
    }

    // 일반사용자 OAuth 연동
    @PostMapping("/api/users/link/{provider}")
    public void linkOauth(@PathVariable final String provider, @AuthenticationPrincipal final CustomUserDetails userDetails
    //public void linkOauth(@PathVariable String provider, @AuthenticationPrincipal UserPrincipal principal
            , HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        request.getSession().setAttribute("OAUTH_LINK_INFO", new UserOAuthLinkVO(userDetails.getUserNo(), provider));
        response.sendRedirect("/oauth2/authorization/" + provider);
    }
}
