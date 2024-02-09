package kz.baltabayev.mapper;

import kz.baltabayev.dto.MeterReadingDto;
import kz.baltabayev.model.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeterReadingMapper {

    MeterReadingMapper INSTANCE = Mappers.getMapper(MeterReadingMapper.class);

    MeterReadingDto toDto(MeterReading meterReading);

    MeterReading toEntity(MeterReadingDto dto);

}
