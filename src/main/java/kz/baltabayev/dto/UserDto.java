package kz.baltabayev.dto;

import kz.baltabayev.model.types.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    String login;
    Role role;
}
