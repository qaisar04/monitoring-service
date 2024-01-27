package kz.baltabayev.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a meter reading entry capturing information about a user's recorded meter readings.
 * Each meter reading entry includes a unique identifier, counter number, reading date, meter type ID, and user ID.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {
    /**
     * The unique identifier for the meter reading entry.
     */
    private Long id;
    /**
     * The counter number associated with the meter reading.
     */
    private Integer counterNumber;
    /**
     * The date when the meter reading was recorded.
     */
    private String readingDate;
    /**
     * The unique identifier for the meter type associated with the reading.
     */
    private Long typeId;
    /**
     * The user ID associated with the meter reading.
     */
    private Long userId;
}