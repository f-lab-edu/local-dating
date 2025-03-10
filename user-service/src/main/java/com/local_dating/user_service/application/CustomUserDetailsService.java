package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /*public CustomUserDetailsService(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }*/
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        return userRepository.findByLoginId(username)
        //return userRepository.findByUserId(username)
                .map(s -> {
                    final List<String> roles = new ArrayList<>();
                    roles.add("USER");
                    return new CustomUserDetails(s.getNo(), s.getLoginId(), s.getPwd());
                    /*return org.springframework.security.core.userdetails.User.builder()
                            //.username(String.valueOf(s.getId()))
                            .username(s.getLoginId())
                            .password(s.getPwd())
                            .roles(roles.toArray(new String[0]))
                            .build();*/
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        /*User user = userRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPwd())
                .roles(roles.toArray(new String[0]))
                .build();*/
    }

    @Transactional
    public void registerUser(@Valid final UserDTO dto) {

        userRepository.findByLoginId(userMapper.INSTANCE.toUserVO(dto).loginId()).ifPresentOrElse(el -> {
        //userRepository.findById(userMapper.INSTANCE.toUserVO(dto).no()).ifPresentOrElse(el -> {
        //userRepository.findById(userMapper.INSTANCE.toUserVO(dto).id()).ifPresentOrElse(el -> {
        //userRepository.findByUserId(userMapper.INSTANCE.toUserVO(dto).userId()).ifPresentOrElse(el -> {
            throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.loginId());
            //throw new UserAlreadyExistsException(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage() + ": " + dto.userId());
        }, () -> {
            userRepository.save(new User(userMapper.INSTANCE.toUserVO(dto)));
        });

        /*User user = new User(userMapper.INSTANCE.toUserVO(dto));
        userRepository.findByUserid(user.getUserId());
        userRepository.save(user);*/
    }

    /*public String save(UserVO vo) {
        User user = new User(vo);
        userRepository.save(user);
        return "z";
    }*/
}
