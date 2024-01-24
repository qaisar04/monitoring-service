package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.model.MeterReading;

import java.util.*;

public class MeterReadingDAOImpl implements MeterReadingDAO {

    private final Map<Long, MeterReading> meterReadings = new HashMap<>();
    private Long id = 1L;

    @Override
    public Optional<MeterReading> findById(Long id) {
        MeterReading meterReading = meterReadings.get(id);
        return meterReading == null ? Optional.empty() : Optional.of(meterReading);
    }

    @Override
    public Collection<MeterReading> findAll() {
        return new ArrayList<>(meterReadings.values());
    }

    @Override
    public MeterReading save(MeterReading meterReading) {
        meterReading.setId(id);
        id++;
        meterReadings.put(meterReading.getId(), meterReading);
        return meterReadings.get(meterReading.getId());
    }

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
