package kz.baltabayev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a meter type with information such as a unique identifier and the type name.
 *
 * @author qaisar
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MeterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String typeName;
}
