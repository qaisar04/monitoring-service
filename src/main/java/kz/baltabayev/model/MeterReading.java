package kz.baltabayev.model;

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
    private String readingDate;
    private Long typeId;
    private Long userId;
}