package kz.baltabayev.mapper;

import kz.baltabayev.dto.MeterReadingDto;
import kz.baltabayev.model.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between MeterReading and MeterReadingDto objects.
 */
@Mapper
public interface MeterReadingMapper {

    /**
     * Converts a MeterReading entity to a MeterReadingDto.
     * @param meterReading The MeterReading entity to convert.
     * @return The corresponding MeterReadingDto.
     */
    MeterReadingDto toDto(MeterReading meterReading);

    /**
     * Converts a MeterReadingDto to a MeterReading entity.
     * Note: The "id" and "userId" properties are ignored during mapping.
     * @param dto The MeterReadingDto to convert.
     * @return The corresponding MeterReading entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    MeterReading toEntity(MeterReadingDto dto);
}
