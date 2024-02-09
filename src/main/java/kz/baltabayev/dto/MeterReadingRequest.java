package kz.baltabayev.dto;

public record MeterReadingRequest(Integer counterNumber,
                                  Long meterTypeId,
                                  Long userId) {
}
