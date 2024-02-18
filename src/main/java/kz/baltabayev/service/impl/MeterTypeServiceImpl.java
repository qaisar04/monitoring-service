package kz.baltabayev.service.impl;

import kz.baltabayev.repository.MeterTypeRepository;
import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.service.MeterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link MeterTypeService} interface.
 */
@Service
@RequiredArgsConstructor
public class MeterTypeServiceImpl implements MeterTypeService {

    private final MeterTypeRepository meterTypeRepository;

    /**
     * Retrieves a list of available meter types.
     *
     * @return a list of available meter types
     */
    @Override
    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeRepository.findAll();
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

        return meterTypeRepository.save(meterType);
    }
}
