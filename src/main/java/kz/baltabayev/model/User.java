package kz.baltabayev.model;

import kz.baltabayev.model.types.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Represents a user with information such as a unique identifier, login, registration date, password, and role.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    /**
     * The unique identifier for the user.
     */
    Long id;
    /**
     * The login name of the user.
     */
    String login;
    /**
     * The registration date of the user.
     */
    @Builder.Default
    LocalDateTime registrationDate = LocalDateTime.now();
    /**
     * The password associated with the user.
     */
    String password;
    /**
     * The role assigned to the user. Default value is {@link Role#USER}.
     */
    @Builder.Default
    Role role = Role.USER;
}
