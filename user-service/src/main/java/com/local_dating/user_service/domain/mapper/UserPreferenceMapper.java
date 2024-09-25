package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.vo.UserPreferenceVO;
import com.local_dating.user_service.presentation.dto.UserPreferenceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserPreferenceMapper {
    UserPreferenceMapper INSTANCE = Mappers.getMapper(UserPreferenceMapper.class);

    UserPreferenceVO toUserPreferenceVO(UserPreferenceDTO userPreferenceDTO);
    UserPreferenceVO toUserPreferenceVO(UserPreference userPreference);
    List<UserPreferenceVO> toUserPreferenceVOList(List<UserPreferenceDTO> userPreferenceDTOList);
    List<UserPreferenceDTO> toUserPreferenceDTOList(List<UserPreferenceVO> userPreferenceVOList);
}
