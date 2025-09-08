package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.type.RoleType;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(@Valid final UserDTO dto) {

        userRepository.findByLoginId(userMapper.INSTANCE.toUserVO(dto).loginId()).ifPresentOrElse(el -> {
            throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.loginId());
        }, () -> {
            User user = new User(userMapper.INSTANCE.toUserVO(dto), passwordEncoder.encode(dto.pwd()));
            user.setRole(RoleType.USER);
            userRepository.save(user);
        });

    }
}
