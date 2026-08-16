package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserRegisterService;
import com.local_dating.user_service.presentation.dto.CiCheckDTO;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.MessageCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final UserRegisterService userRegisterService;

    @PostMapping(value = "/api/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입", description = "사용자 데이터 생성 및 코인데이터 생성")
    public String register(@RequestBody @Valid final UserDTO user, final CiCheckDTO ciCheckDTO) {
        userRegisterService.registerUser(user, ciCheckDTO);
        return MessageCode.REGISTER_SUCCESS.getMessage();
    }

    @PostMapping(value = "/api/users/ci")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "CI 확인", description = "CI 확인 및 가입확인")
    public String getCiInfo(@RequestBody @Valid final CiCheckDTO dto) {
        return userRegisterService.getCiInfo(dto);
    }

}
