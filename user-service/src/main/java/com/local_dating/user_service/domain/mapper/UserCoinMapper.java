package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.vo.UserCoinVO;
import com.local_dating.user_service.presentation.dto.UserCoinDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserCoinMapper {

    UserCoinMapper INSTANCE = Mappers.getMapper(UserCoinMapper.class);

    UserCoinVO toUserCoinVO(UserCoinDTO userCoinDTO);
}
