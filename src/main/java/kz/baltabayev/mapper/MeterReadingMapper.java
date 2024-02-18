package kz.baltabayev.mapper;

import kz.baltabayev.dto.MeterReadingDto;
import kz.baltabayev.model.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeterReadingMapper {

    MeterReadingDto toDto(MeterReading meterReading);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    MeterReading toEntity(MeterReadingDto dto);
}
