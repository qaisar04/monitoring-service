package kz.baltabayev.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a meter type with information such as a unique identifier and the type name.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MeterType {
    /**
     * The unique identifier for the meter type.
     */
    Long id;
    /**
     * The name associated with the meter type.
     */
    String typeName;
}
