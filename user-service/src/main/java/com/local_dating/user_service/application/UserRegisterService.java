package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.OAuthInfo;
import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.type.RegisterType;
import com.local_dating.user_service.domain.type.RoleType;
import com.local_dating.user_service.domain.vo.CheckCiValidationVO;
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

import java.util.List;
import java.util.UUID;
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

        CheckCiValidationVO ciValidationObj = checkCiValidation(ciCheckDTO);
        //boolean isNewUser = checkCiValidation(ciCheckDTO);

        if (userRepository.existsByLoginId(userMapper.INSTANCE.toUserVO(dto).loginId())) {
            throw new UserAlreadyExistsException(MessageCode.DATA_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.loginId());
        }

        if (Boolean.TRUE.equals(ciValidationObj.isResult())) { // 신규가입
            User userNew = new User(userMapper.INSTANCE.toUserVO(dto), passwordEncoder.encode(dto.pwd()));
            userNew.setRole(RoleType.USER);
            userNew.setRegisterType(RegisterType.MANUAL.name());
            userNew.setCi(ciValidationObj.getCi());
            User saved = userRepository.save(userNew);
            userCoinService.saveNewCoinData(saved.getNo());
        } else if (Boolean.FALSE.equals(ciValidationObj.isResult())) {
            User user = userRepository.findById(Long.valueOf(ciValidationObj.getUserNo()))
                    .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));
            user.setLoginId(dto.loginId());
            user.setPwd(passwordEncoder.encode(dto.pwd()));
            user.setNickname(dto.nickname());
            user.setBirth(dto.birth());
            user.setPhone(dto.phone());
            user.setRegisterType(RegisterType.SOCIAL.name());
            userRepository.save(user);
        } else {
            throw new BusinessException(MessageCode.UNKNOWN_EXCEPTION);
        }
    }

    private CheckCiValidationVO checkCiValidation(CiCheckDTO ciCheckDTO) {

        CheckCiValidationVO returnObj = new CheckCiValidationVO();

        String ciCheckToken = ciCheckDTO.token();
        String ci;
        if (ciCheckToken == null || ciCheckToken.isEmpty()) {
            returnObj.setCi(null);
            returnObj.setResult(true);
            return returnObj;
        }

        // CI 인증을 거친 회원가입 루트
        ci = stringRedisTemplate.opsForValue().getAndDelete("ciCheck:" + ciCheckToken);

        if (ci == null) {
            // 만료되었거나 유효하지 않은 상태
            throw new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION);
        }

        return userRepository.findByCi(ci)
                .map(user -> {
                    List<OAuthInfo> authInfoList = oAuthInfoRepository.findByUserNo(user.getNo());

                    if (!authInfoList.isEmpty() && user.getPwd() == null) {
                        // OAuth 가입자
                        returnObj.setResult(false);
                        returnObj.setUserNo(user.getNo());
                        return returnObj;
                    }

                    // 이미 일반회원으로 가입되어 있음
                    throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + user.getLoginId());
                })
                .orElseGet(() -> {
                    // 해당 CI로 가입된 사용자가 없음
                    returnObj.setResult(true);
                    returnObj.setCi(ci);
                    return returnObj;
                });


        /*
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
        */
    }

    public String getCiInfo(CiCheckDTO dto) {
        String ci = ciGeneratorUtil.generateCi(dto.hpNo());

        userRepository.findByCi(ci).ifPresent(el -> {
            if (el.getPwd() != null) {
                throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.hpNo());
            }
        });

        /*if (userRepository.findByCi(ci).isPresent()) {
            userRepository.findByCi(ci).filter(el -> el.getPwd() != null)
                    .orElseThrow(() -> new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.hpNo()));
        }*/
        String ciCheckToken = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set("ciCheck:" + ciCheckToken, ci, 20, TimeUnit.MINUTES);
        return ciCheckToken;

        /*return userRepository.findByCi(ci).map(el -> {
            throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.hpNo());
        }).orElseGet(() -> {
            String ciCheckToken = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("ciCheck:" + ciCheckToken, ci, 20, TimeUnit.MINUTES);
            return ciCheckToken;
        });*/
        //return null;
    }

}
