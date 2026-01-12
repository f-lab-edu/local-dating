package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserProfileCore;
import com.local_dating.user_service.domain.mapper.UserProfileCoreMapper;
import com.local_dating.user_service.domain.vo.UserPreferenceCoreVO;
import com.local_dating.user_service.domain.vo.UserProfileCoreVO;
import com.local_dating.user_service.infrastructure.repository.UserProfileCoreRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileCoreService {

    private final UserRepository userRepository;
    private final UserProfileCoreRepository userProfileCoreRepository;
    private final UserProfileCoreMapper userProfileCoreMapper;
    //private final UserProfileCoreRepositoryCustomImpl userProfileCoreRepositoryCustom;

    public UserProfileCoreVO viewProfileCore(final Long userId) {
        return userProfileCoreRepository.findByUserId(userId).map(userProfileCoreMapper::userProfileCoreToUserProfileCoreVo)
                .orElseThrow(() -> {
                    throw new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION);
                });
    }

    @Transactional
    public void saveProfileCore(final UserProfileCoreVO userProfileCoreVO) {

        userRepository.findById(userProfileCoreVO.userId()).orElseThrow(() -> {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        });

        userProfileCoreRepository.findByUserId(userProfileCoreVO.userId()).map(el -> {
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

    public List<UserProfileCoreVO> searchNext(final Long userNo, final UserPreferenceCoreVO userPreferenceCoreVO) {
    //public List<UserProfileCore> searchNext(final Long userNo, final UserPreferenceCoreVO userPreferenceCoreVO) {
        int searchLimit = 100;
        return userProfileCoreMapper.userProfileCoreListToUserProfileCoreVoList(userProfileCoreRepository.searchNextUsers(userNo, userPreferenceCoreVO, searchLimit));
        //return userProfileCoreRepository.searchNextUsers(userNo, userPreferenceCoreVO, searchLimit);
        //userProfileCoreRepositoryCustom.searchCandidates(userNo, userPreferenceCoreVO, searchLimit);
    }
}
