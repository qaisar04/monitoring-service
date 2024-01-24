package kz.baltabayev.model;

import kz.baltabayev.model.types.MeterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {
    private Long id;
    private Integer counterNumber;
    private MeterType meterType;
    private String readingDate;
    private Long userId;
}