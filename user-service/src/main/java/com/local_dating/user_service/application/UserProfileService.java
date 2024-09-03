package com.local_dating.user_service.application;

import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserProfileService {
    public int saveProfile(final String userId, final List<UserProfileDTO> userProfileDTO);
}
