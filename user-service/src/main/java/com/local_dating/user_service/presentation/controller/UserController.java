package com.local_dating.user_service.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.local_dating.user_service.application.*;
import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.mapper.UserPreferenceMapper;
import com.local_dating.user_service.domain.mapper.UserProfileMapper;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.presentation.dto.UserPreferenceDTO;
import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import com.local_dating.user_service.util.JwtUtil;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.local_dating.user_service.util.MessageCode.DATA_NOT_FOUND_EXCEPTION;

@RestController
//@RequiredArgsConstructor
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
    private final UserPreferenceService userPreferenceService;
    private final UserProfileMapper userProfileMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final KafkaProducer kafkaProducer;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserCardService userCardService;

    public UserController(final CustomUserDetailsService customUserDetailsService
            , final AuthenticationManager authenticationManager
            , JwtUtil jwtUtil
            , UserProfileService userProfileService
            , UserPreferenceService userPreferenceService
            , UserProfileMapper userProfileMapper
            , UserPreferenceMapper userPreferenceMapper, KafkaProducer kafkaProducer, RestTemplate restTemplate, ObjectMapper objectMapper, UserCardService userCardService) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userProfileService = userProfileService;
        this.userPreferenceService = userPreferenceService;
        this.userProfileMapper = userProfileMapper;
        this.userPreferenceMapper = userPreferenceMapper;
        this.kafkaProducer = kafkaProducer;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.userCardService = userCardService;
        this.objectMapper.registerModule(new ParameterNamesModule()).findAndRegisterModules();
    }

    @PostMapping(value = "/v1/user/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid final UserDTO user) {
    //public ResponseEntity<String> register(@RequestBody @Valid final UserDTO user) {

        customUserDetailsService.registerUser(user);
        return MessageCode.REGISTER_SUCCESS.getMessage();
        //return ResponseEntity.status(HttpStatus.CREATED).body(MessageCode.REGISTER_SUCCESS.getMessage());
        //return new ResponseEntity<>(MessageCode.REGISTER_SUCCESS.getMessage(), HttpStatus.OK);
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

    @PostMapping(value = "/v1/user/login")
    public LoginRes login(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {
    //public ResponseEntity login(@RequestBody @Valid final UserDTO userDTO) {

        final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.userId() , userDTO.pwd()));
        final String userId = authentication.getName();
        final UserVO user = new UserVO(userId, userDTO.pwd(), userDTO.name(), userDTO.nickname(), userDTO.birth(), userDTO.phone());
        final String token = jwtUtil.createToken(user);
        final LoginRes loginRes = new LoginRes(userId, token);

        try {
            kafkaProducer.sentLoginLog("my-topic", objectMapper.writeValueAsString(new UserLoginLogVO(userId, request.getRemoteAddr(), "N", LocalDateTime.now())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //kafkaProducer.sentLoginLog("my-topic", new UserLoginLogVO(userId, request.getRemoteAddr(), "N", LocalDateTime.now()));
        //kafkaProducer.sendMessage("my-topic", "Login Success for userId: " + userId);

        return loginRes;
        //return ResponseEntity.ok(loginRes);
        //return ResponseEntity.ok(new LoginRes(authentication.getName(), jwtUtil.createToken(new UserVO(userId, userDTO.pwd(), userDTO.name(), userDTO.birth(), userDTO.phone()))));

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


    @PostMapping(value = "/v1/user/profile")
    @ResponseStatus(HttpStatus.CREATED)
    //@deprecated
    public void saveProfile(final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTO) {
    //public ResponseEntity saveProfile(final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTO) {
    //public String profile(final Authentication authentication, @RequestBody final UserProfileDTO userProfileDTO) {

        String userId = (String) authentication.getPrincipal();
        userProfileService.saveProfile(userId, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTO));
        //userProfileService.saveProfile(userId, userProfileDTO);

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userId = userDetails.getUsername(); // 보통 username이 사용자 ID로 사용됨
        }*/

        //return ResponseEntity.status(HttpStatus.CREATED).build();
        //return new ResponseEntity<>(HttpStatus.CREATED);
        //return "토큰정보: " + userId;
    }

    @PatchMapping(value = "/v1/users/profile")
    public void updateProfile(final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTOList) {
    //public ResponseEntity updateProfile(final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTOList) throws Exception {
        //Optional.of((String) authentication.getPrincipal()).map(s -> userProfileService.updateProfile(s, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTOList)))
        String userId = (String) authentication.getPrincipal();
        userProfileService.updateProfile(userId, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTOList));
        //userProfileService.updateProfile(userId, userProfileDTOList);

        //return ResponseEntity.ok().build();
        //return new ResponseEntity<>(HttpStatus.OK);
    }

    /*@PostMapping(value = "/profile")
    public String profile(Authentication authentication) {
        return "Hello, " + authentication.getName() + "!";
    }*/

    @GetMapping(value = "/v1/users/profile")
    public List viewProfile(final Authentication authentication) {
    //public ResponseEntity<?> viewProfile(final Authentication authentication) {

        return Optional.of(userProfileService.viewProfile((String) authentication.getPrincipal()))
                .map(list -> UserProfileMapper.INSTANCE.toUserProfileDTOList(list))
                //.map(list -> ResponseEntity.ok(UserProfileMapper.INSTANCE.toUserProfileDTOList(list)))
                .orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage()));
        //return userProfileService.viewProfile((String) authentication.getPrincipal()).map(el -> ResponseEntity.ok(UserProfileMapper.INSTANCE.toUserProfileDTOList(el))).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage())); // Optional 사용 시
        //return userProfileService.viewProfile((String) authentication.getPrincipal()).map(el -> ResponseEntity.ok(UserProfileMapper.INSTANCE.toUserProfileDTO(el))).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage()));
        //return userProfileService.viewProfile((String) authentication.getPrincipal()).map(el -> ResponseEntity.ok(UserProfileMapper.INSTANCE.toUserProfileDTO(el))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserProfileDTO()));

    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity handleException() {
        System.out.println("eerrrrrrrrrrror");
        return new ResponseEntity<>("에러났다", HttpStatus.OK);
    }*/

    @PostMapping(value = "/v1/users/preference")
    public List savePreference(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
    //public ResponseEntity<List<UserPreference>> savePreference(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        List<UserPreference> userPreferenceList = userPreferenceService.savePreferences((String) authentication.getPrincipal(), UserPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
        //userPreferenceService.savePreferences(authentication.getPrincipal(), UserPreferenceMapper.INSTANCE.toUserPreferenceVO());
        return userPreferenceList;
        //return ResponseEntity.status(HttpStatus.CREATED).body(userPreferenceList);
    }

    @PutMapping(value = "/v1/users/preference")
    public void updatePreference(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
    //public ResponseEntity updatePreference(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) throws Exception {
        userPreferenceService.updatePreferences(authentication.getPrincipal().toString(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
        //return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/v1/users/preference")
    public String viewPreference(final Authentication authentication) {

        String result = userPreferenceService.viewPreference((String) authentication.getPrincipal());
        if (result.isEmpty()) {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage());
        }
        return result;
        /*return Optional.of(userPreferenceService.viewPreference((String) authentication.getPrincipal()))
                .map(list -> ResponseEntity.ok(UserPreferenceMapper.INSTANCE.toUserPreferenceDTOList(list)))
                .orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage()));*/
    }

    /*@GetMapping(value = "/preference")
    public List<UserPreferenceDTO> viewPreference(final Authentication authentication) {

        List<UserPreferenceVO> userPreferenceVOList = userPreferenceService.viewPreference((String) authentication.getPrincipal());
        if (userPreferenceVOList.isEmpty()) {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage());
        }
        return UserPreferenceMapper.INSTANCE.toUserPreferenceDTOList(userPreferenceVOList);
    }*/

    @PatchMapping(value = "/v1/users/preference/prior")
    public void updatePreferencePriority(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        userPreferenceService.updatePreferencesPriority(authentication.getPrincipal().toString(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
    }

    /*@GetMapping(value = "/v1/users/cards")
    public List viewRecomcard(final Authentication authentication) {
        return userCardService.getCard(authentication.getPrincipal().toString());
    }

    @PostMapping(value = "/v1/users/cards")
    public void saveRecomcard(final Authentication authentication) {
        userCardService.setCard(authentication.getPrincipal().toString());
    }*/
}
