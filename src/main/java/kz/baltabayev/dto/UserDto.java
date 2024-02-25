package kz.baltabayev.dto;

import kz.baltabayev.model.types.Role;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing user information.
 * This class encapsulates data related to a user, including the login and role.
 */
@Getter
@Setter
public class UserDto {
    /**
     * The login name of the user.
     */
    String login;

    /**
     * The role of the user.
     */
    Role role;
}
