package kz.baltabayev.model;

import kz.baltabayev.model.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String login;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String registrationDate;
    private String password;
    private String address;
    private Role role;
}
