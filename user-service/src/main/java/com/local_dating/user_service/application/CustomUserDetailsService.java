package com.local_dating.user_service.application;

import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String loginId) throws UsernameNotFoundException {

        return userRepository.findByLoginId(loginId)
                .map(el -> {
                    //final List<String> roles = new ArrayList<>();
                    //roles.add("USER");

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(el.getRole().getCode()));

                    return new CustomUserDetails(el.getNo(), el.getLoginId(), el.getPwd(), authorities);
                })
                .orElseThrow(() -> new BusinessException(MessageCode.USER_NOT_FOUND));
    }

    public UserDetails loadUserByUserNo(final Long userNo) throws UsernameNotFoundException {

        return userRepository.findById(userNo)
                .map(el -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();

                    String raw = el.getRole().getCode();
                    String springRole = raw != null && raw.startsWith("ROLE_") ? raw : "ROLE_" + raw; // 접두어 달기
                    authorities.add(new SimpleGrantedAuthority(springRole));
                    //authorities.add(new SimpleGrantedAuthority(el.getRole().getCode()));
                    return new CustomUserDetails(el.getNo(), el.getLoginId(), el.getPwd(), authorities);
                })
                .orElseThrow(() -> new BusinessException(MessageCode.USER_NOT_FOUND));
    }

}
