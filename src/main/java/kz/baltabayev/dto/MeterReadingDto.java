package kz.baltabayev.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing meter reading information.
 * This class encapsulates data related to a meter reading, including the counter number,
 * reading date, and type ID.
 */
@Data
public class MeterReadingDto {
    /**
     * The counter number associated with the meter reading.
     */
    Integer counterNumber;

    /**
     * The date of the meter reading in string format.
     */
    String readingDate;

    /**
     * The ID of the meter type associated with the meter reading.
     */
    Long typeId;
}
