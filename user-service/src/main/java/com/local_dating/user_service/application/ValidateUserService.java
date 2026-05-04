package com.local_dating.user_service.application;

import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateUserService {

    private final UserRepository userRepository;

    public void validateUserId(final Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new BusinessException(MessageCode.USER_NOT_FOUND, userId.toString()));
    }
}
