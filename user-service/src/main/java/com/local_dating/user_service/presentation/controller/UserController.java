package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.CustomUserDetailsService;
import com.local_dating.user_service.application.UserProfileService;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import com.local_dating.user_service.util.JwtUtil;
import com.local_dating.user_service.util.MessageCode;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@PropertySource("classpath:message/message.properties")
public class UserController {

    /*@Value("${api.response.user.register.success}")
    private String registerSuccess;

    @Value("${api.response.user.register.fail}")
    private String registerFail;
*/
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserProfileService userProfileService;

    public UserController(final CustomUserDetailsService customUserDetailsService, final AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserProfileService userProfileService) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userProfileService = userProfileService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody @Valid final UserDTO user) throws Exception {

        customUserDetailsService.registerUser(user);
        return new ResponseEntity<>(MessageCode.REGISTER_SUCCESS.getMessage(), HttpStatus.OK);
        //return new ResponseEntity<>(registerSuccess, HttpStatus.OK);

        /*try {
            customUserDetailsService.registerUser(user);
            return new ResponseEntity<>(registerSuccess, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(registerFail, HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
    }



    /*@PostMapping(value = "/user")
    public UserDTO getUser(String userId) {
        UserVO vo = service.getUser(userId);
        CoinVO coin = service.getCoin(userId);
        UserInfoDTO dto = mapper.toUserDTO(vo, coin)
        return dto;
    }*/

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody @Valid final UserDTO userDTO) {

        final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.userId() , userDTO.pwd()));
        final String userId = authentication.getName();
        final UserVO user = new UserVO(userId, userDTO.pwd(), userDTO.name(), userDTO.birth(), userDTO.phone());
        final String token = jwtUtil.createToken(user);
        final LoginRes loginRes = new LoginRes(userId, token);

        //return ResponseEntity.ok(new LoginRes(authentication.getName(), jwtUtil.createToken(new UserVO(userId, userDTO.pwd(), userDTO.name(), userDTO.birth(), userDTO.phone()))));
        return ResponseEntity.ok(loginRes);

        /*try {
            final Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.userId() , userDTO.pwd()));
            final String userId = authentication.getName();
            final UserVO user = new UserVO(userId, userDTO.pwd(), userDTO.name(), userDTO.birth(), userDTO.phone());
            final String token = jwtUtil.createToken(user);
            final LoginRes loginRes = new LoginRes(userId, token);

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }*/
    }


    @PostMapping(value = "/profile")
    public String profile(final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTO) {
    //public String profile(final Authentication authentication, @RequestBody final UserProfileDTO userProfileDTO) {

        String userId = (String) authentication.getPrincipal();
        userProfileService.saveProfile(userId, userProfileDTO);

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userId = userDetails.getUsername(); // 보통 username이 사용자 ID로 사용됨
        }*/

        return "토큰정보: " + userId;
    }

    /*@PostMapping(value = "/profile")
    public String profile(Authentication authentication) {
        return "Hello, " + authentication.getName() + "!";
    }*/

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity handleException() {
        System.out.println("eerrrrrrrrrrror");
        return new ResponseEntity<>("에러났다", HttpStatus.OK);
    }*/


}
