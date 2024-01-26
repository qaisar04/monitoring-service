package kz.baltabayev.service;

import kz.baltabayev.model.MeterType;

import java.util.List;

public interface MeterTypeService {
    List<MeterType> showAvailableMeterTypes();

    MeterType save(MeterType meterType);
}
