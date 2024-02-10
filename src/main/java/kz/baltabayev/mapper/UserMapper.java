package kz.baltabayev.mapper;

import kz.baltabayev.dto.UserDto;
import kz.baltabayev.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "login", source = "login")
    @Mapping(target = "role", source = "role")
    UserDto toDto(User entity);

    User toEntity(UserDto userDto);

}
