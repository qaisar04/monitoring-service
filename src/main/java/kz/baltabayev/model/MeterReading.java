package kz.baltabayev.model;

import kz.baltabayev.model.types.MeterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {
    private Integer counterNumber;
    private MeterType meterType;
    private LocalDate submissionDate;
    private Integer consumptionAmount;
    private User user;
}