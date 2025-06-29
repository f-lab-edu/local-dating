package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.vo.UserValidationVO;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

//@RequiredArgsConstructor
@Slf4j
@Service
public class UserVerificationService {

    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisTemplate<String, Long> redisTemplateLong;
    
    private final UserMapper userMapper;

    private final MessageService messageService;

    public UserVerificationService(UserRepository userRepository, @Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate, @Qualifier("redisStringLongTemplate") RedisTemplate<String, Long> redisTemplateLong
            , UserMapper userMapper, MessageService messageService) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.redisTemplateLong = redisTemplateLong;
        this.userMapper = userMapper;
        this.messageService = messageService;
    }

    public UserValidationVO getVerificationCode(final UserValidationVO UserValidationVO) {
    //public String getVerificationCode(final UserValidationDTO userValidationDTO) {
    //public String getVerificationCode(final UserValidationDTO userValidationDTO) {

        User user = this.verifyUserByPhone(UserValidationVO.phone());
        String code = this.createVerificationCode();
        redisTemplate.opsForValue().set("userValidationCode:" + user.getNo(), code, 180, TimeUnit.SECONDS);
        redisTemplateLong.delete("userValidationCodeFailCnt:" + user.getNo());

        log.info("getVerificationCode 시간: " + LocalDateTime.now());

        // 메일 등 처리
        messageService.sendVerificationEmail(user.getEmail(), code)
                .exceptionally(ex -> {
                    log.error("이메일 발송 중 예외 발생: {}", ex.getMessage());
                    return null;
                });
        //messageService.sendVerificationEmail(user.getEmail(), code);

        return userMapper.UserToUserValidationVO(user, code);
        //return new UserValidationVO(user.getNo(), code);
        //return "인증번호는 " + code + "입니다";
    }

    public User verifyUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
    }

    public User verifyUserByPhone(final String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
    }

    public void checkVerificationCode(final String code, final Long id) {
        String redisCode = redisTemplate.opsForValue().get("userValidationCode:" + id);
        Long failCnt = redisTemplateLong.opsForValue().get("userValidationCodeFailCnt:" + id);
        if (!code.equals(redisCode)) {
            if (failCnt == null) {
                redisTemplateLong.opsForValue().set("userValidationCodeFailCnt:" + id, 1L);
                throw new BusinessException(MessageCode.INVALID_VERIFICATION_CODE);
            } else {
                redisTemplateLong.opsForValue().increment("userValidationCodeFailCnt:" + id);
            }
            if (failCnt >= 4) {
                throw new BusinessException(MessageCode.EXCEEDED_MAX_ATTEMPTS);
            } else {
                throw new BusinessException(MessageCode.INVALID_VERIFICATION_CODE);
            }
        } else {
            redisTemplate.delete("userValidationCode:" + id);
            redisTemplateLong.delete("userValidationCodeFailCnt:" + id);
        }
    }

    public String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000; // 100000~999999 사이 6자리 숫자
        return String.valueOf(code);
    }

    /*public String sendVerificationCode(final String code, final Long id) {
        User user = verifyUserById(id);

        checkVerificationCode(code, id);

        return user.getLoginId();
    }*/

    @Async
    public CompletableFuture<User> verifyUserByIdAsync(final Long id) {
        return CompletableFuture.completedFuture(verifyUserById(id));
    }

    @Async
    public CompletableFuture<Void> checkVerificationCodeAsync(final String code, final Long id) {
        checkVerificationCode(code, id);
        return CompletableFuture.completedFuture(null);
    }

    public String sendVerificationCode(final String code, final Long id) {
        CompletableFuture<User> userF = verifyUserByIdAsync(id);
        CompletableFuture<Void> checkF = checkVerificationCodeAsync(code, id);

        // 두 Future가 모두 완료될 때까지 기다리고, 예외 언래핑
        try {
            CompletableFuture.allOf(userF, checkF).join();
        } catch (CompletionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof BusinessException) {
                throw (BusinessException) cause;
            }
            throw ex;
        }

        return userF.join().getLoginId();
    }

}
