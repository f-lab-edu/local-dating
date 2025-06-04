package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserVerificationService;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.presentation.dto.UserValidationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class VerificationController {

    private final UserVerificationService userVerificationService;

    private final UserMapper userMapper;

    @PostMapping("/v1/auth/get-code")
    public UserValidationDTO getVerificationCode(@RequestBody @Valid final UserValidationDTO userValidationDTO) {
        return userMapper.UserValidationVOToUserValidationDTO(
                userVerificationService.getVerificationCode(userMapper.UserValidationDTOToUserValidationVO(userValidationDTO))
        );
        //return userVerificationService.getVerificationCode(userValidationDTO);
    }

    @GetMapping("/v1/auth/send-code/{code}/id/{id}")
    public String sendVerificationCode(@PathVariable("code") final String code, @PathVariable("id") final Long id) {
        return userVerificationService.sendVerificationCode(code, id);
    }
}
