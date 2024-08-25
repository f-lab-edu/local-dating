package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.mapper.UserMapper;
import com.local_dating.user_service.infrastructure.respository.UserRepository;
import com.local_dating.user_service.presentation.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CustomUserDetailsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserid(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUserid())
                        .password(user.getPwd())
                        .roles(roles.toArray(new String[0]))
                        .build();
    }

    public void registerUser(UserDTO dto) throws Exception {
    //public String save(UserDTO dto) throws Exception {
        User user = new User(userMapper.INSTANCE.toUserVO(dto));
        userRepository.save(user);
        /*userRepository.save(user);
        return "z";*/
    }

    /*public String save(UserVO vo) {
        User user = new User(vo);
        userRepository.save(user);
        return "z";
    }*/




}
