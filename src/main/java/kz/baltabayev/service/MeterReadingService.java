package kz.baltabayev.service;

import kz.baltabayev.model.MeterReading;

import java.util.List;

/**
 * The service interface for meter reading functionality.
 */
public interface MeterReadingService {

    /**
     * Retrieves the current meter readings for the specified user.
     *
     * @param userId the ID of the user
     * @return the list of current meter readings
     */
    List<MeterReading> getCurrentMeterReadings(Long userId);

    /**
     * Submits a meter reading for the specified user.
     *
     * @param counterNumber the counter number for the meter reading
     * @param meterTypeId   the ID of the meter type
     * @param userId        the ID of the user
     */
    void submitMeterReading(Integer counterNumber, Long meterTypeId, Long userId);

    /**
     * Retrieves meter readings for a specific month and year for the specified user.
     *
     * @param year   the year of the readings
     * @param month  the month of the readings
     * @param userId the ID of the user
     * @return the list of meter readings for the specified month and year
     */
    List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId);

    /**
     * Retrieves the meter reading history for the specified user.
     *
     * @param userId the ID of the user
     * @return the list of meter reading history
     */
    List<MeterReading> getMeterReadingHistory(Long userId);
}
