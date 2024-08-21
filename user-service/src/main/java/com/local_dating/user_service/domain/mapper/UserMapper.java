@Mapper 1
public interface UserMapper {
 
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class ); 3
 
    
    UserVO toUserVO(UserDTO dto);
    UserDTO toUserVO(UserVO vo); 
}
