package kz.baltabayev.model.entity;

import jakarta.persistence.*;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meter_reading", schema = "develop")
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meter_reading_id_seq")
    @SequenceGenerator(name = "meter_reading_id_seq", sequenceName = "develop.meter_reading_id_seq", allocationSize = 1)
    private Long id;
    private Integer counterNumber;
    private String readingDate;
    private Long typeId;
    private Long userId;
}