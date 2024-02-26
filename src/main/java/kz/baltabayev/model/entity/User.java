package kz.baltabayev.model.entity;

import jakarta.persistence.*;
import kz.baltabayev.model.types.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String login;
    @Builder.Default
    LocalDateTime registrationDate = LocalDateTime.now();
    String password;
    @Enumerated(EnumType.STRING)
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
