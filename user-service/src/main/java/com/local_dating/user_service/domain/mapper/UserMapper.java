package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
 
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
 
    
    UserVO toUserVO(UserDTO dto);

    UserDTO toUserDTO(UserVO vo); 

    UserVO toUserVO(User vo);
    /*
    UserDTO toUserDTO(UserEntity vo);  --> X 꼭 하는 사람들이 있어요..... -> 이건 코드리뷰로 막아줘야 한다. 안그럼 규칙이 다깨진다..
    */
}
