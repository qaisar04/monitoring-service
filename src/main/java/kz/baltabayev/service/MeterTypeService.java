package kz.baltabayev.service;

import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.MeterType;

import java.util.List;

/**
 * The service interface for meter type functionality.
 */
public interface MeterTypeService {

    /**
     * Retrieves the list of available meter types.
     *
     * @return the list of available meter types
     */
    List<MeterType> showAvailableMeterTypes();

    /**
     * Saves a meter type.
     *
     * @param meterType the meter type to be saved
     * @return the saved meter type
     */
    MeterType save(MeterTypeRequest meterType);
}
