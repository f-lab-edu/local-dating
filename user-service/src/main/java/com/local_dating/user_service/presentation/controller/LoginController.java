package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserInterceptorLoginService;
import com.local_dating.user_service.application.UserLoginService;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final UserInterceptorLoginService userInterceptorLoginService;

    @PostMapping(value = "/v1/users/login") // 컨트롤러 기반 로그인(스프링시큐리티)
    public LoginRes login(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {

        return userLoginService.login(userMapper.INSTANCE.toUserVO(userDTO), request);
    }

    @PostMapping(value = "/v1/interceptor/users/login") // 컨트롤러 기반 로그인(스프링시큐리티X, 인터셉터)
    public LoginRes loginInterceptor(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {

        return userInterceptorLoginService.login(userMapper.INSTANCE.toUserVO(userDTO), request);
        //return userInterceptorLoginService.login(userDTO.loginId(), req.password(), request);
        //return userLoginService.login(userMapper.INSTANCE.toUserVO(userDTO), request);
    }

    @PostMapping("/v1/users/{id}/refresh")
    public LoginRes refresh(@RequestHeader("Authorization") String authentication, HttpServletRequest request, final @PathVariable("id") long id) {

        return userLoginService.refreshTokens(jwtUtil.resolveRefreshToken(authentication), request, id);
    }

}
