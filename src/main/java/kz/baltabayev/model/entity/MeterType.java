package kz.baltabayev.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a meter type with information such as a unique identifier and the type name.
 *
 * @author qaisar
 */
@Data
@Entity
@Builder
@Table(name = "meter_type", schema = "develop")
@NoArgsConstructor
@AllArgsConstructor
public class MeterType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meter_type_id_seq")
    @SequenceGenerator(name = "meter_type_id_seq", sequenceName = "develop.meter_type_id_seq", allocationSize = 1)
    private Long id;
    private String typeName;
}
