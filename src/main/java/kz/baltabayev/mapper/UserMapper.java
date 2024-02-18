package kz.baltabayev.mapper;

import kz.baltabayev.dto.UserDto;
import kz.baltabayev.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    @Mapping(target = "login", source = "login")
    @Mapping(target = "role", source = "role")
    UserDto toDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto userDto);
}
