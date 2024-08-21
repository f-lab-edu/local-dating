@Mapper 1
public interface UserMapper {
 
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
 
    
    UserVO toUserVO(UserDTO dto);
    UserDTO toUserDTO(UserVO vo); 

    UserVO toUserVO(UserEntity vo); 
    UserDTO toUserDTO(UserEntity vo);  --> X 꼭 하는 사람들이 있어요..... -> 이건 코드리뷰로 막아줘야 한다. 안그럼 규칙이 다깨진다..
}
