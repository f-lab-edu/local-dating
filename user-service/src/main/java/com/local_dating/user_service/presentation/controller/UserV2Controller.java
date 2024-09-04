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
public class UserV2Controller {

    /*@Value("${api.response.user.register.success}")
    private String registerSuccess;

    @Value("${api.response.user.register.fail}")
    private String registerFail;
*/
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Qualfier("UserProfileV2ServiceImpl")
    private final UserProfileService userProfileService;

    public UserController(final CustomUserDetailsService customUserDetailsService, final AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserProfileService userProfileService) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userProfileService = userProfileService;
    }



    @PostMapping(value = "/v2/profile")
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


}
