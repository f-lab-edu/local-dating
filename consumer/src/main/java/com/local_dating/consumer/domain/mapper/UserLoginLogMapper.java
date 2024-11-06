package com.local_dating.consumer.domain.mapper;

import com.local_dating.consumer.domain.entity.UserLoginLog;
import com.local_dating.consumer.domain.vo.UserLoginLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserLoginLogMapper {
    UserLoginLogMapper INSTANCE = Mappers.getMapper(UserLoginLogMapper.class);

    UserLoginLog toUserLoginLog(UserLoginLogVO userLoginLogVO);
}
