package kz.baltabayev.service.impl;

import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.service.MeterTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MeterTypeServiceImpl implements MeterTypeService {

    private final MeterTypeDAO meterTypeDAO;

    @Override
    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeDAO.findAll();
    }

    @Override
    public MeterType save(MeterType meterType) {
        return meterTypeDAO.save(meterType);
    }
}
