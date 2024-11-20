package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.UserRecomCard;
import com.local_dating.user_service.domain.vo.UserRecomCardVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRecomCardMapper {

    UserRecomCardMapper INSTANCE = Mappers.getMapper(UserRecomCardMapper.class);
    UserRecomCardVO toUserRecomCardVO(UserRecomCard userRecomCard);
    List<UserRecomCardVO> toUserRecomCardVOs(List<UserRecomCard> userRecomCards);
}
