package kz.baltabayev.service;

import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.entity.MeterType;

import java.util.List;

/**
 * The service interface for meter type functionality.
 */
public interface MeterTypeService {

    List<MeterType> showAvailableMeterTypes();

    MeterType save(MeterTypeRequest meterType);
}
