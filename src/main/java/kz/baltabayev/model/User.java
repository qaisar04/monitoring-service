package kz.baltabayev.model;

import kz.baltabayev.model.types.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
public class User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var l = List.of(new SimpleGrantedAuthority("ROLE_".concat(role.name())));
        System.out.println(l.get(0).getAuthority());
        return l;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
