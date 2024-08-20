package com.local_dating.user_service.presentation;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/register")
    public String register(@RequestBody User user) {
        userRepository.save(user);
        return "register";
    }

    @PostMapping(value = "/login")
    public String login() {
        return "login";
    }


}