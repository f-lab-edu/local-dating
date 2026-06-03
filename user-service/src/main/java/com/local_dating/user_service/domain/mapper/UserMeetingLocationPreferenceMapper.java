package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserMeetingLocationPreference;
import com.local_dating.user_service.domain.vo.UserMeetingLocationPreferenceVO;
import com.local_dating.user_service.presentation.dto.UserMeetingLocationPreferenceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMeetingLocationPreferenceMapper {

    UserMeetingLocationPreferenceMapper INSTANCE = Mappers.getMapper(UserMeetingLocationPreferenceMapper.class);

    UserMeetingLocationPreferenceVO toVo(UserMeetingLocationPreference entity);

    List<UserMeetingLocationPreferenceVO> toVoList(List<UserMeetingLocationPreference> entityList);

    UserMeetingLocationPreferenceDTO toDto(UserMeetingLocationPreferenceVO vo);

    List<UserMeetingLocationPreferenceDTO> toDtoList(List<UserMeetingLocationPreferenceVO> voList);

    UserMeetingLocationPreferenceVO toVo(UserMeetingLocationPreferenceDTO dto);
}
