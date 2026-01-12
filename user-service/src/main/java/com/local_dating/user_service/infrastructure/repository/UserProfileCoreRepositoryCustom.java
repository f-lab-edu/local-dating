package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserProfileCore;
import com.local_dating.user_service.domain.vo.UserPreferenceCoreVO;

import java.util.List;

public interface UserProfileCoreRepositoryCustom {
    List<UserProfileCore> searchNextUsers(Long userNo, UserPreferenceCoreVO userPreferenceCoreVO, int limit);
}
