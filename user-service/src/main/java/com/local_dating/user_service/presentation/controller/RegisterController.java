package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.CustomUserDetailsService;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.MessageCode;
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

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping(value = "/v1/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid final UserDTO user) {
        customUserDetailsService.registerUser(user);
        return MessageCode.REGISTER_SUCCESS.getMessage();
    }
}
