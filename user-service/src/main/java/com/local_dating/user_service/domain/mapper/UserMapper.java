package com.local_dating.user_service.domain.mapper;

import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.domain.vo.UserValidationVO;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.presentation.dto.UserValidationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    UserValidationVO UserVOToUserValidationVO(UserVO vo);

    UserValidationVO UserValidationDTOToUserValidationVO(UserValidationDTO userValidationDTO);

    @Mapping(target = "code", source = "code")
    UserValidationVO UserToUserValidationVO(User user, String code);

    UserValidationDTO UserValidationVOToUserValidationDTO(UserValidationVO userValidationVO);
}
