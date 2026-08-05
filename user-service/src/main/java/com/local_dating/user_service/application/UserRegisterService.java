package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.OAuthInfo;
import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.type.RegisterType;
import com.local_dating.user_service.domain.type.RoleType;
import com.local_dating.user_service.infrastructure.repository.OAuthInfoRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.CiCheckDTO;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.CiGeneratorUtil;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import com.local_dating.user_service.util.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final UserCoinService userCoinService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CiGeneratorUtil ciGeneratorUtil;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    public void registerUser(@Valid final UserDTO dto, final CiCheckDTO ciCheckDTO) {

        Map<String, String> ciValidationObj = checkCiValidation(ciCheckDTO);
        //boolean isNewUser = checkCiValidation(ciCheckDTO);

        userRepository.findByLoginId(userMapper.INSTANCE.toUserVO(dto).loginId())
                .orElseThrow(() -> new UserAlreadyExistsException(MessageCode.DATA_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.loginId()));

        if (ciValidationObj.get("result").equals("true")) {
            User userNew = new User(userMapper.INSTANCE.toUserVO(dto), passwordEncoder.encode(dto.pwd()));
            userNew.setRole(RoleType.USER);
            userNew.setRegisterType(RegisterType.MANUAL.name());
            userNew.setCi(ciValidationObj.get("ci"));
            User saved = userRepository.save(userNew);
            userCoinService.saveNewCoinData(saved.getNo());
        } else if (ciValidationObj.get("result").equals("false")) {
            User user = userRepository.findById(Long.valueOf(ciValidationObj.get("userNo")))
                    .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
            user.setLoginId(dto.loginId());
            user.setPwd(passwordEncoder.encode(dto.pwd()));
            user.setNickname(dto.nickname());
            user.setBirth(dto.birth());
            user.setPhone(dto.phone());
            user.setRegisterType(RegisterType.SOCIAL.name());
            User saved = userRepository.save(user);
            userCoinService.saveNewCoinData(saved.getNo());
        } else {
            throw new BusinessException(MessageCode.UNKNOWN_EXCEPTION);
        }
    }

    private Map<String, String> checkCiValidation(CiCheckDTO ciCheckDTO) {
    //private boolean checkCiValidation(CiCheckDTO ciCheckDTO) {

        Map<String, String> obj = new HashMap<>();

        String ci = stringRedisTemplate.opsForValue().get("ciCheck:" + ciCheckDTO.token());
        return userRepository.findByCi(ci).map(el -> {
            List<OAuthInfo> authInfoList = oAuthInfoRepository.findByUserNo(el.getNo());

            if (!authInfoList.isEmpty() && el.getPwd() == null) { // Oauth 가입자, 일반 회원가입x
                obj.put("result", "false");
                obj.put("userNo", el.getNo().toString());
                return obj;
            } else { // Oauth 미가입자, 일반 회원가입자 or // Oauth 가입자, 일반 회원가입자 -> 일반회원 가입안시킴
                throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + el.getLoginId());
            }

        }).orElseGet(() -> {
            obj.put("result", "true");
            obj.put("ci", ci);
            return obj;
        }); // 완전 신규회원
    }

    public String getCiInfo(CiCheckDTO dto) {
        String ci = ciGeneratorUtil.generateCi(dto.hpNo());
        userRepository.findByCi(ci).map(el -> {
            throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.hpNo());
        }).orElseGet(() -> {
            String ciCheckToken = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("ciCheck:" + ciCheckToken, ci, 20, TimeUnit.MINUTES);
            return ciCheckToken;
        });
        return null;
    }

}
