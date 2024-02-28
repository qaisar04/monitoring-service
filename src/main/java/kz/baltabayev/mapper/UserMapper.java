package kz.baltabayev.mapper;

import kz.baltabayev.dto.UserDto;
import kz.baltabayev.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper interface for converting between User and UserDto objects.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Converts a User entity to a UserDto.
     * @param entity The User entity to convert.
     * @return The corresponding UserDto.
     */
    @Mapping(target = "login", source = "login")
    @Mapping(target = "role", source = "role")
    UserDto toDto(User entity);

    /**
     * Converts a UserDto to a User entity.
     * Note: The "id", "registrationDate", and "password" properties are ignored during mapping.
     * @param userDto The UserDto to convert.
     * @return The corresponding User entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto userDto);
}
