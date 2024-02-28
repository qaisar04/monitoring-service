package kz.baltabayev.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing meter reading information.
 * This class encapsulates data related to a meter reading, including the counter number,
 * reading date, and type ID.
 */
public record MeterReadingDto
        (
                Integer counterNumbe,
                String readingDate,
                Long typeId
        ) {
}
