package kz.baltabayev.service;

import kz.baltabayev.model.MeterReading;

import java.util.List;

public interface MeterReadingService {
    List<MeterReading> getCurrentMeterReadings(Long userId);

    void submitMeterReading(Integer counterNumber, Long meterTypeId, Long userId);

    List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId);

    List<MeterReading> getMeterReadingHistory(Long userId);
}
