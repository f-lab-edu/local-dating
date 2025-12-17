package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserPreferenceCore;
import com.local_dating.user_service.domain.mapper.UserPreferenceCoreMapper;
import com.local_dating.user_service.domain.vo.UserPreferenceCoreVO;
import com.local_dating.user_service.infrastructure.repository.UserPreferenceCoreRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferenceCoreService {

    private final UserRepository userRepository;
    private final UserPreferenceCoreRepository userPreferenceCoreRepository;
    private final UserPreferenceCoreMapper userPreferenceCoreMapper;

    public UserPreferenceCoreVO  viewPreferenceCore(final Long userId) {
        return userPreferenceCoreRepository.findByUserId(userId).map(userPreferenceCoreMapper::userPreferenceCoreToUserPreferenceVo)
                .orElseThrow(() -> {
                    throw new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION);
                });
    }

    @Transactional
    public void savePreferenceCore(final UserPreferenceCoreVO userPreferenceCoreVO) {

        userRepository.findById(userPreferenceCoreVO.userId()).orElseThrow(() -> {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        });

        userPreferenceCoreRepository.findByUserId(userPreferenceCoreVO.userId()).map(el->{
            el.setBirthMin(userPreferenceCoreVO.birthMin());
            el.setBirthMax(userPreferenceCoreVO.birthMax());
            el.setHeightMin(userPreferenceCoreVO.heightMin());
            el.setHeightMax(userPreferenceCoreVO.heightMax());
            el.setSalaryMin(userPreferenceCoreVO.salaryMin());
            el.setSalaryMax(userPreferenceCoreVO.salaryMax());
            el.setRangeMax(userPreferenceCoreVO.rangeMax());
            return el;
        }).orElseGet(() -> userPreferenceCoreRepository.save(new UserPreferenceCore(userPreferenceCoreVO)));
    }
}
