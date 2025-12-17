package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserPreferenceCore;
import com.local_dating.user_service.domain.vo.UserPreferenceCoreVO;
import com.local_dating.user_service.presentation.dto.UserPreferenceCoreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserPreferenceCoreMapper {

    UserPreferenceCoreMapper INSTANCE = Mappers.getMapper(UserPreferenceCoreMapper.class);

    UserPreferenceCoreDTO userPreferenceVoToUserPreferenceCoreDto(UserPreferenceCoreVO vo);

    UserPreferenceCoreVO userPreferenceCoreToUserPreferenceVo(UserPreferenceCore entity);

    UserPreferenceCoreVO UserPreferenceCoreDtoToUserPreferenceVo(UserPreferenceCoreDTO dto);
}
