package kz.baltabayev.service.impl;

import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.service.MeterTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementation of the {@link MeterTypeService} interface.
 */
@RequiredArgsConstructor
public class MeterTypeServiceImpl implements MeterTypeService {

    private final MeterTypeDAO meterTypeDAO;

    /**
     * Retrieves a list of available meter types.
     *
     * @return a list of available meter types
     */
    @Override
    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeDAO.findAll();
    }

    /**
     * Saves a new meter type.
     *
     * @param meterType the meter type to be saved
     * @return the saved meter type
     */
    @Override
    public MeterType save(MeterTypeRequest request) {
        MeterType meterType = MeterType.builder().typeName(request.typeName())
                .build();

        return meterTypeDAO.save(meterType);
    }
}
