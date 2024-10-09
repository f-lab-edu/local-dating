package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserLoginLog;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserLoginLogMapper {
    UserLoginLogMapper INSTANCE = Mappers.getMapper(UserLoginLogMapper.class);

    UserLoginLog toUserLoginLog(UserLoginLogVO userLoginLogVO);
}
