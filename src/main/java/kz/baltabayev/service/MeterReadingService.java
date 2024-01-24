package kz.baltabayev.service;

import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.types.MeterType;

import java.util.List;

public interface MeterReadingService {
    List<MeterReading> getCurrentMeterReadings(Long userId);

    void submitMeterReading(Integer counterNumber, MeterType meterType, Long userId);

    List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId);

    List<MeterReading> getMeterReadingHistory(Long userId);


}
