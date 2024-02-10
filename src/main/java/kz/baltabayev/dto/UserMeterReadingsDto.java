package kz.baltabayev.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserMeterReadingsDto {
    private UserDto userDto;
    private List<MeterReadingDto> meterReadings;
}