package kz.baltabayev.model;

import kz.baltabayev.model.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user with information such as a unique identifier, login, registration date, password, and role.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /**
     * The unique identifier for the user.
     */
    private Long id;
    /**
     * The login name of the user.
     */
    private String login;
    /**
     * The registration date of the user.
     */
    private String registrationDate;
    /**
     * The password associated with the user.
     */
    private String password;
    /**
     * The role assigned to the user. Default value is {@link Role#USER}.
     */
    @Builder.Default
    private Role role = Role.USER;
}
