package kz.baltabayev.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a meter type with information such as a unique identifier and the type name.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterType {
    /**
     * The unique identifier for the meter type.
     */
    private Long id;
    /**
     * The name associated with the meter type.
     */
    private String typeName;
}
