package kz.baltabayev.dto;

import kz.baltabayev.model.types.Role;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing user information.
 * This class encapsulates data related to a user, including the login and role.
 */
public record UserDto
        (
                String login,
                Role role
        ) {
}
