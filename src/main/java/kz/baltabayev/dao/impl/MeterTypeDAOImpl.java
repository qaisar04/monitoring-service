package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.model.MeterType;

import java.util.*;

/**
 * Implementation of the MeterTypeDAO interface using an in-memory map.
 * Provides methods for CRUD operations on MeterType entities and initializes
 * the map with predefined MeterType entities during construction.
 */
public class MeterTypeDAOImpl implements MeterTypeDAO {

    // In-memory storage for MeterType entities
    private final Map<Long, MeterType> meterTypes = new HashMap<>();

    // Variable to generate unique IDs for MeterType entities
    private Long id = 1L;

    /**
     * Constructs a new MeterTypeDAOImpl and initializes the in-memory storage
     * with predefined MeterType entities.
     */
    public MeterTypeDAOImpl() {
        save(MeterType.builder().typeName("HEATING").build());
        save(MeterType.builder().typeName("COLD_WATER").build());
        save(MeterType.builder().typeName("HOT_WATER").build());
    }

    /**
     * Retrieves a MeterType entity by its ID.
     *
     * @param id The ID of the MeterType entity to retrieve.
     * @return An Optional containing the found MeterType entity, or an empty Optional if not found.
     */
    @Override
    public Optional<MeterType> findById(Long id) {
        MeterType meterType = meterTypes.get(id);
        return meterType == null ? Optional.empty() : Optional.of(meterType);
    }

    /**
     * Retrieves a list of all MeterType entities.
     *
     * @return A list containing all MeterType entities stored in the in-memory map.
     */
    @Override
    public List<MeterType> findAll() {
        return new ArrayList<>(meterTypes.values());
    }

    /**
     * Saves a MeterType entity to the in-memory storage.
     *
     * @param type The MeterType entity to save.
     * @return The saved MeterType entity with an assigned ID.
     */
    @Override
    public MeterType save(MeterType type) {
        type.setId(id);
        id++;
        meterTypes.put(type.getId(), type);
        return meterTypes.get(type.getId());
    }
}
