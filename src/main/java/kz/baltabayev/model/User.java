package kz.baltabayev.model;

import kz.baltabayev.model.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String login;
    private String registrationDate;
    private String password;
    @Builder.Default
    private Role role = Role.USER;
}
