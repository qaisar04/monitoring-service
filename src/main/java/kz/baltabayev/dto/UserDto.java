package kz.baltabayev.dto;

import kz.baltabayev.model.types.Role;

import java.time.LocalDateTime;

public class UserDto {
    String login;
    LocalDateTime registrationDate;
    Role role;
}
