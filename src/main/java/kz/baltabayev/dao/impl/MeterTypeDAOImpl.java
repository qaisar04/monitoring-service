package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.model.MeterType;

import java.util.*;

public class MeterTypeDAOImpl implements MeterTypeDAO {

    private final Map<Long, MeterType> meterTypes = new HashMap<>();
    private Long id = 1L;

    public MeterTypeDAOImpl() {
        save(
                MeterType.builder()
                        .typeName("ОТОПЛЕНИЕ")
                        .build()
        );
        save(
                MeterType.builder()
                        .typeName("ХОЛОДНАЯ ВОДА")
                        .build()
        );
        save(
                MeterType.builder()
                        .typeName("ГОРЯЧАЯ ВОДА")
                        .build()
        );
    }

    @Override
    public Optional<MeterType> findById(Long id) {
        MeterType meterType = meterTypes.get(id);
        return meterType == null ? Optional.empty() : Optional.of(meterType);
    }

    @Override
    public List<MeterType> findAll() {
        return new ArrayList<>(meterTypes.values());
    }

    @Override
    public MeterType save(MeterType type) {
        type.setId(id);
        id++;
        meterTypes.put(type.getId(), type);
        return meterTypes.get(type.getId());
    }
}
