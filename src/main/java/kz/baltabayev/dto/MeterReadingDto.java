package kz.baltabayev.dto;

import lombok.Data;

@Data
public class MeterReadingDto {
    Integer counterNumber;
    String readingDate;
    Long typeId;
}
