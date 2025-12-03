package com.local_dating.user_service.application;

import com.local_dating.user_service.application.async.UserVerificationAsyncTask;
import com.local_dating.user_service.application.common.UserVerificationCommonService;
import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.vo.UserValidationVO;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserVerificationService {

    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    @Qualifier("redisStringLongTemplate")
    private final RedisTemplate<String, Long> redisTemplateLong;
    private final UserMapper userMapper;
    private final MessageService messageService;
    private final UserVerificationCommonService userVerificationCommonService;
    private final UserVerificationAsyncTask userVerificationAsyncTask;

    public UserVerificationService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate
            , @Qualifier("redisStringLongTemplate") RedisTemplate<String, Long> redisTemplateLong
            , UserMapper userMapper, MessageService messageService, UserVerificationCommonService userVerificationCommonService
            , UserVerificationAsyncTask userVerificationAsyncTask) {
        this.redisTemplate = redisTemplate;
        this.redisTemplateLong = redisTemplateLong;
        this.userMapper = userMapper;
        this.messageService = messageService;
        this.userVerificationCommonService = userVerificationCommonService;
        this.userVerificationAsyncTask = userVerificationAsyncTask;
    }

    public UserValidationVO getVerificationCode(final UserValidationVO UserValidationVO) {

        User user = userVerificationCommonService.verifyUserByPhone(UserValidationVO.phone());
        String code = this.createVerificationCode();
        redisTemplate.opsForValue().set("userValidationCode:" + user.getPhone(), code, 180, TimeUnit.SECONDS);
        //redisTemplate.opsForValue().set("userValidationCode:" + user.getNo(), code, 180, TimeUnit.SECONDS);
        redisTemplateLong.delete("userValidationCodeFailCnt:" + user.getPhone());

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


    public String checkVerificationCode(final UserValidationVO userValidationVO) {
        CompletableFuture<User> userF = userVerificationAsyncTask.verifyUserByPhoneAsync(userValidationVO.phone());
        CompletableFuture<Void> checkF = userVerificationAsyncTask.checkVerificationCodeAsync(userValidationVO.code(), userValidationVO.phone());

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

    public String sendVerificationCode(final String code, final Long id) {
        CompletableFuture<User> userF = userVerificationAsyncTask.verifyUserByIdAsync(id);
        CompletableFuture<Void> checkF = userVerificationAsyncTask.checkVerificationCodeAsync(code, id);

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
