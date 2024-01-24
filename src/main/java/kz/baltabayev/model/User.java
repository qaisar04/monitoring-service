package kz.baltabayev.model;

import kz.baltabayev.model.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private BigDecimal balance;
    private LocalDate registrationDate;
    private String password;
    private String address;
    private List<MeterReading> meterReadings;
    private Role role;
}
