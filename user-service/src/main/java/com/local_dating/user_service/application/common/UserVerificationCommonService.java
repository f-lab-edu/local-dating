package com.local_dating.user_service.application.common;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationCommonService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Long> redisTemplateLong;

    public UserVerificationCommonService(UserRepository userRepository
            , @Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate
            , @Qualifier("redisStringLongTemplate") RedisTemplate<String, Long> redisTemplateLong) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.redisTemplateLong = redisTemplateLong;
    }

    public User verifyUserByPhone(final String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
    }

    public User verifyUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
    }

    public void checkVerificationCode(final String code, final String phone) {
        String redisCode = redisTemplate.opsForValue().get("userValidationCode:" + phone);
        Long failCnt = redisTemplateLong.opsForValue().get("userValidationCodeFailCnt:" + phone);

        if (failCnt != null && failCnt >= 5) {
            throw new BusinessException(MessageCode.EXCEEDED_MAX_ATTEMPTS);
        }

        if (!code.equals(redisCode)) {
            if (failCnt == null) {
                redisTemplateLong.opsForValue().set("userValidationCodeFailCnt:" + phone, 1L);
                throw new BusinessException(MessageCode.INVALID_VERIFICATION_CODE);
            }
            redisTemplateLong.opsForValue().increment("userValidationCodeFailCnt:" + phone);

            throw new BusinessException(MessageCode.INVALID_VERIFICATION_CODE);

        }
        redisTemplate.delete("userValidationCode:" + phone);
        redisTemplateLong.delete("userValidationCodeFailCnt:" + phone);
    }

    public void checkVerificationCode(final String code, final Long id) {
        String redisCode = redisTemplate.opsForValue().get("userValidationCode:" + id);
        Long failCnt = redisTemplateLong.opsForValue().get("userValidationCodeFailCnt:" + id);

        if (failCnt != null && failCnt >= 5) {
            throw new BusinessException(MessageCode.EXCEEDED_MAX_ATTEMPTS);
        }

        if (!code.equals(redisCode)) {
            if (failCnt == null) {
                redisTemplateLong.opsForValue().set("userValidationCodeFailCnt:" + id, 1L);
                throw new BusinessException(MessageCode.INVALID_VERIFICATION_CODE);
            }
            redisTemplateLong.opsForValue().increment("userValidationCodeFailCnt:" + id);


            throw new BusinessException(MessageCode.INVALID_VERIFICATION_CODE);

        }
        redisTemplate.delete("userValidationCode:" + id);
        redisTemplateLong.delete("userValidationCodeFailCnt:" + id);
    }
}
