package kz.baltabayev.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a meter reading entry capturing information about a user's recorded meter readings.
 * Each meter reading entry includes a unique identifier, counter number, reading date, meter type ID, and user ID.
 *
 * @author qaisar
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer counterNumber;
    String readingDate;
    Long typeId;
    Long userId;
}