package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserProfileCore;
import com.local_dating.user_service.domain.vo.UserProfileCoreVO;
import com.local_dating.user_service.presentation.dto.UserProfileCoreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileCoreMapper {

    UserProfileCoreMapper INSTANCE = Mappers.getMapper(UserProfileCoreMapper.class);

    UserProfileCoreVO toUserProfileCoreVo(UserProfileCoreDTO dto);

    UserProfileCoreVO userProfileCoreToUserProfileCoreVo(UserProfileCore entity);

    UserProfileCoreDTO userProfileVoToUserProfileCoreDto(UserProfileCoreVO vo);
}
