package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserMeetingLocationPreference;
import com.local_dating.user_service.domain.mapper.UserMeetingLocationPreferenceMapper;
import com.local_dating.user_service.domain.vo.UserMeetingLocationPreferenceVO;
import com.local_dating.user_service.infrastructure.repository.UserMeetingLocationPreferenceRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMeetingLocationPreferenceService {

    private final UserRepository userRepository;
    private final UserMeetingLocationPreferenceRepository userMeetingLocationPreferenceRepository;
    private final UserMeetingLocationPreferenceMapper userMeetingLocationPreferenceMapper;

    public List<UserMeetingLocationPreferenceVO> viewPreferences(final Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        });
        return userMeetingLocationPreferenceMapper.toVoList(userMeetingLocationPreferenceRepository.findByUserIdAndActiveYnOrderByPriorityAsc(userId, "Y"));
    }

    @Transactional
    public void savePreference(final Long userId, final UserMeetingLocationPreferenceVO userMeetingLocationPreferenceVO) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        });

        if (userMeetingLocationPreferenceRepository.countByUserIdAndActiveYn(userId, "Y") >= 5) {
            throw new BusinessException(MessageCode.DATA_LIMIT_EXCEEDED_EXCEPTION);
        }

        userMeetingLocationPreferenceRepository.save(new UserMeetingLocationPreference(userMeetingLocationPreferenceVO));
    }

    @Transactional
    public void updatePreference(final Long userId, final Long preferenceId, final UserMeetingLocationPreferenceVO userMeetingLocationPreferenceVO) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        });

        UserMeetingLocationPreference preference = userMeetingLocationPreferenceRepository.findByIdAndUserId(preferenceId, userId)
                .filter(el -> "Y".equals(el.getActiveYn()))
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));

        preference.setAreaName(userMeetingLocationPreferenceVO.areaName());
        preference.setAddress(userMeetingLocationPreferenceVO.address());
        preference.setAddressDetail1(userMeetingLocationPreferenceVO.addressDetail1());
        preference.setAddressDetail2(userMeetingLocationPreferenceVO.addressDetail2());
        preference.setLatitude(userMeetingLocationPreferenceVO.latitude());
        preference.setLongitude(userMeetingLocationPreferenceVO.longitude());
        preference.setPriority(userMeetingLocationPreferenceVO.priority());
        preference.setActiveYn("Y");
        preference.setModUser(userId);
        preference.setModDate(LocalDateTime.now());

        //return userMeetingLocationPreferenceMapper.toVo(preference);
    }

}
