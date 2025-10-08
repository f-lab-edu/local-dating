package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserProfileCore;
import com.local_dating.user_service.domain.vo.UserProfileCoreVO;
import com.local_dating.user_service.infrastructure.repository.UserProfileCoreRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileCoreService {

    private final UserRepository userRepository;
    private final UserProfileCoreRepository userProfileCoreRepository;

    @Transactional
    public void saveProfileCore(final UserProfileCoreVO userProfileCoreVO) {

        userRepository.findById(userProfileCoreVO.userId()).orElseThrow(() -> {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        });

        userProfileCoreRepository.findById(userProfileCoreVO.userId()).map(el -> {
                    el.setGender(userProfileCoreVO.gender());
                    el.setBirth(userProfileCoreVO.birth());
                    el.setHeight(userProfileCoreVO.height());
                    el.setEducation(userProfileCoreVO.education());
                    el.setSalary(userProfileCoreVO.salary());
                    el.setEducation(userProfileCoreVO.education());
                    return el;
                })
                .orElseGet(() -> userProfileCoreRepository.save(new UserProfileCore(userProfileCoreVO)));
    }
}
