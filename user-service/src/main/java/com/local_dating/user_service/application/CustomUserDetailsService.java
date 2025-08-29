package com.local_dating.user_service.application;

import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
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
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        return userRepository.findByLoginId(username)
                .map(el -> {
                    //final List<String> roles = new ArrayList<>();
                    //roles.add("USER");

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(el.getRole().name()));

                    return new CustomUserDetails(el.getNo(), el.getLoginId(), el.getPwd(), authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException(MessageCode.USER_NOT_FOUND.getMessage()));
                //.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
