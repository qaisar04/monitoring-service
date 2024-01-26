package kz.baltabayev.dao;

import kz.baltabayev.model.MeterReading;

import java.util.List;

public interface MeterReadingDAO extends MainDAO<Long, MeterReading> {

    List<MeterReading> findAllByUserId(Long userId);
}
