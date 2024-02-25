package kz.baltabayev.service;

import kz.baltabayev.model.MeterReading;

import java.util.List;

/**
 * The service interface for meter reading functionality.
 */
public interface MeterReadingService {

    List<MeterReading> getCurrentMeterReadings(String login);

    MeterReading submitMeterReading(Integer counterNumber, Long meterTypeId, String login);

    List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, String login);

    List<MeterReading> getMeterReadingHistory(String login);
}
