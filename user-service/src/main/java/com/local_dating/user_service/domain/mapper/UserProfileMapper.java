package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserProfile;
import com.local_dating.user_service.domain.vo.UserProfileVO;
import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);
    UserProfileVO toUserProfileVO(UserProfileDTO dto);
    UserProfileVO toUserProfileVO(UserProfile entity);
    UserProfileDTO toUserProfileDTO(UserProfileVO vo);
    List<UserProfileDTO> toUserProfileDTOList(List<UserProfileVO> vo);
}
