package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.domain.type.RegisterType;
import com.local_dating.user_service.domain.type.RoleType;
import com.local_dating.user_service.infrastructure.repository.OAuthInfoRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final UserCoinService userCoinService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(@Valid final UserDTO dto) {

        userRepository.findByLoginId(userMapper.INSTANCE.toUserVO(dto).loginId())
                .orElseThrow(() -> new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.loginId()));

        // oauth 가입검사
        Optional<User> checkUser = userRepository.findByEmail(userMapper.INSTANCE.toUserVO(dto).email());

        if (checkUser.isPresent() && checkUser.get().getPwd() == null) { // user 테이블에 정보가 있음
            oAuthInfoRepository.findByUserNoAndProvider(checkUser.get().getNo(), "google")
                    .orElseThrow(() -> new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + checkUser.get().getLoginId()));

            User user = checkUser.get();
            user.setLoginId(dto.loginId());
            user.setPwd(passwordEncoder.encode(dto.pwd()));
            user.setNickname(dto.nickname());
            user.setBirth(dto.birth());
            user.setPhone(dto.phone());
            user.setRegisterType(RegisterType.SOCIAL.name());
            User saved = userRepository.save(user);
            userCoinService.saveNewCoinData(saved.getNo());
        } else { // 일반가입
            User userNew = new User(userMapper.INSTANCE.toUserVO(dto), passwordEncoder.encode(dto.pwd()));
            userNew.setRole(RoleType.USER);
            userNew.setRegisterType(RegisterType.MANUAL.name());
            //user.setRegisterType(RegisterType.MANUAL.getCode());
            User saved = userRepository.save(userNew);
            userCoinService.saveNewCoinData(saved.getNo());
        }




        //Optional<User> checkUser = userRepository.findByEmail(userMapper.INSTANCE.toUserVO(dto).email());

        /*
        if (checkUser.isPresent()) { // oauth 가입함
            User user = checkUser.get();
            user.setLoginId(dto.loginId());
            user.setPwd(passwordEncoder.encode(dto.pwd()));
            user.setNickname(dto.nickname());
            user.setBirth(dto.birth());
            user.setPhone(dto.phone());
            user.setRegisterType(RegisterType.SOCIAL.name());
            User saved = userRepository.save(user);
            userCoinService.saveNewCoinData(saved.getNo());
        } else {
            User userNew = new User(userMapper.INSTANCE.toUserVO(dto), passwordEncoder.encode(dto.pwd()));
            userNew.setRole(RoleType.USER);
            userNew.setRegisterType(RegisterType.MANUAL.name());
            //user.setRegisterType(RegisterType.MANUAL.getCode());
            User saved = userRepository.save(userNew);
            userCoinService.saveNewCoinData(saved.getNo());
        }
        */

        /*userRepository.findByLoginId(userMapper.INSTANCE.toUserVO(dto).loginId()).ifPresentOrElse(el -> {
            throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.loginId());
        }, () -> {
            User user = new User(userMapper.INSTANCE.toUserVO(dto), passwordEncoder.encode(dto.pwd()));
            user.setRole(RoleType.USER);
            User saved = userRepository.save(user);
            userCoinService.saveNewCoinData(saved.getNo());
        });*/

    }
}
