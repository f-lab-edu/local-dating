package com.local_dating.user_service.presentation;

import com.local_dating.user_service.application.CustomUserDetailsService;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.ErrorRes;
import com.local_dating.user_service.presentation.dto.UserDTO;

import com.local_dating.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:message/message.properties")
public class UserController {

    @Value("${api.response.user.register.success}")
    private String registerSuccess;

    @Value("${api.response.user.register.fail}")
    private String registerFail;

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        //UserVO vo = mapper.toUserVO(user);

        //customUserDetailsService.save(vo);
        //userRepository.save(vo); 레포지토리는 어플리케이션 영역에서 호출해야함
        try {
            customUserDetailsService.registerUser(user);
            return new ResponseEntity<>(registerSuccess, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(registerFail, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PostMapping(value = "/user")
    public UserDTO getUser(String userId) {
        UserVO vo = service.getUser(userId);
        CoinVO coin = service.getCoin(userId);
        UserInfoDTO dto = mapper.toUserDTO(vo, coin)
        return dto;
    }*/

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
    //public String login(@RequestBody UserDTO userDTO) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.userid() , userDTO.pwd()));
            String userid = authentication.getName();
            UserVO user = new UserVO(userid, userDTO.pwd(), userDTO.name(), userDTO.birth(), userDTO.phone());
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = new LoginRes(userid, token);

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /*@PostMapping(value = "/profile")
    public String profile(Authentication authentication) {
        return "Hello, " + authentication.getName() + "!";
    }*/


}
