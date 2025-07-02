package com.local_dating.user_service.presentation.controller;


import com.local_dating.user_service.application.UserLoginService;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/v1/users/login")
    public LoginRes login(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {

        return userLoginService.login(userMapper.INSTANCE.toUserVO(userDTO), request);
        //return userLoginService.login(userDTO, request);

        /*final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.loginId(), userDTO.pwd()));
        final String userId = authentication.getName();
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final UserVO user = new UserVO(userDetails.getId(), userId, userDTO.pwd(), userDTO.name(), userDTO.nickname(), userDTO.birth(), userDTO.phone());
        //final UserVO user = new UserVO(Long.parseLong(userId), userDTO.pwd(), userDTO.name(), userDTO.nickname(), userDTO.birth(), userDTO.phone());
        final String token = jwtUtil.createToken(user);
        final LoginRes loginRes = new LoginRes(userId, token);

        try {
            logger.debug("LoginController 카프카 부분 try");
            kafkaProducer.sentKafkaMsg("login-log-topic", objectMapper.writeValueAsString(new UserLoginLogVO(userDetails.getId(), request.getRemoteAddr(), "N", LocalDateTime.now())));
            //kafkaProducer.sentLoginLog("my-topic", objectMapper.writeValueAsString(new UserLoginLogVO(userId, request.getRemoteAddr(), "N", LocalDateTime.now())));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            logger.debug("LoginController 카프카 부분 finally");
        }

        return loginRes;*/
    }

    @PostMapping("/v1/users/refresh")
    public LoginRes refresh(@RequestHeader("Refresh-Token") String authentication, HttpServletRequest request) {
        String refreshToken = jwtUtil.resolveRefreshToken(authentication);
        return userLoginService.refreshTokens(refreshToken, request); //test2
    }

}
