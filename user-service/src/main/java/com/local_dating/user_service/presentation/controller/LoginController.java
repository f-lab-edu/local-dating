package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserLoginService;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @PostMapping("/v1/users/{id}/refresh")
    public LoginRes refresh(@RequestHeader("Authorization") String authentication, HttpServletRequest request, final @PathVariable("id") long id) {

        return userLoginService.refreshTokens(jwtUtil.resolveRefreshToken(authentication), request, id);
    }

}
