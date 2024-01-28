package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.model.MeterReading;

import java.util.*;

/**
 * Implementation of the MeterReadingDAO interface using an in-memory map.
 * Provides methods for CRUD operations on MeterReading entities and additional
 * method to retrieve all MeterReadings associated with a specific user.
 */
public class MeterReadingDAOImpl implements MeterReadingDAO {

    private final Map<Long, MeterReading> meterReadings = new HashMap<>();
    private Long id = 1L;

    /**
     * Retrieves a MeterReading entity by its ID.
     *
     * @param id The ID of the MeterReading entity to retrieve.
     * @return An Optional containing the found MeterReading entity, or an empty Optional if not found.
     */
    @Override
    public Optional<MeterReading> findById(Long id) {
        MeterReading meterReading = meterReadings.get(id);
        return Optional.ofNullable(meterReading);
    }

    /**
     * Retrieves a list of all MeterReading entities.
     *
     * @return A list containing all MeterReading entities stored in the in-memory map.
     */
    @Override
    public List<MeterReading> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(meterReadings.values()));
    }

    /**
     * Saves a MeterReading entity to the in-memory storage.
     *
     * @param meterReading The MeterReading entity to save.
     * @return The saved MeterReading entity with an assigned ID.
     */
    @Override
    public MeterReading save(MeterReading meterReading) {
        meterReading.setId(id++);
        meterReadings.put(meterReading.getId(), meterReading);
        return meterReadings.get(meterReading.getId());
    }

    /**
     * Retrieves a list of all MeterReading entities associated with a specific user.
     *
     * @param userId The ID of the user for whom to retrieve MeterReadings.
     * @return A list containing all MeterReading entities associated with the specified user.
     */
    @Override
    public List<MeterReading> findAllByUserId(Long userId) {
        List<MeterReading> result = new ArrayList<>();

        for (MeterReading meterReading : meterReadings.values()) {
            if (meterReading.getUserId().equals(userId)) {
                result.add(meterReading);
            }
        }

        return result;
    }
}
