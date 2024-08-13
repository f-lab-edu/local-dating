package com.local_dating.user_service.controller;

import com.local_dating.user_service.entity.User;
import com.local_dating.user_service.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ResponseBody
    @PostMapping(value = "/register")
    public String register(@RequestBody User user) {
        userRepository.save(user);
        return "register";
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public String login() {
        return "login";
    }


}
