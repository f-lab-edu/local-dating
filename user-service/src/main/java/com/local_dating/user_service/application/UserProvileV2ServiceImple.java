package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserProfile;
import com.local_dating.user_service.domain.mapper.UserProfileMapper;
import com.local_dating.user_service.infrastructure.repository.UserProfileRepository;
import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProvileV2ServiceImple implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    @Transactional
    public int saveProfile(final String userId, final List<UserProfileDTO> userProfileDTO) {
    //public int saveProfile(String UserId, UserProfileDTO userProfileDTO) {
        final String nick = userProfileRepository.findByUserId(userId).getNicxk();
        userProfileDTO.stream().forEach(el->userProfileRepository.save(new UserProfile(userId, nick, userProfileMapper.INSTANCE.toUserProfileVO(el))));
        //userProfileRepository.save(new UserProfile(userProfileMapper.INSTANCE.toUserProfileVO(userProfileDTO)));

        return 100;
    }
}
