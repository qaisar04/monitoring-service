package kz.baltabayev.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MeterReading {
    /**
     * The unique identifier for the meter reading entry.
     */
    Long id;
    /**
     * The counter number associated with the meter reading.
     */
    Integer counterNumber;
    /**
     * The date when the meter reading was recorded.
     */
    String readingDate;
    /**
     * The unique identifier for the meter type associated with the reading.
     */
    Long typeId;
    /**
     * The user ID associated with the meter reading.
     */
    Long userId;
}