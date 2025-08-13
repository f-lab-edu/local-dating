package com.local_dating.user_service.application;

import com.local_dating.user_service.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        return userRepository.findByLoginId(username)
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
    }

}
