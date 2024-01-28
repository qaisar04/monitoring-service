package kz.baltabayev.dao;

import kz.baltabayev.model.MeterReading;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing MeterReading entities.
 * Extends the generic MainDAO interface with specific operations for MeterReading entities.
 */
public interface MeterReadingDAO extends MainDAO<Long, MeterReading> {

    /**
     * Retrieves a list of MeterReading entities associated with the specified user ID.
     *
     * @param userId The ID of the user whose MeterReading entities are to be retrieved.
     * @return A list of MeterReading entities associated with the specified user ID.
     */
    List<MeterReading> findAllByUserId(Long userId);
}
