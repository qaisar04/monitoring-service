package kz.baltabayev.service.impl;

import kz.baltabayev.loggingstarter.annotations.LoggableInfo;
import kz.baltabayev.loggingstarter.annotations.LoggableTime;
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
@LoggableInfo
@RequiredArgsConstructor
public class MeterTypeServiceImpl implements MeterTypeService {

    private final MeterTypeRepository meterTypeRepository;

    @Override
    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeRepository.findAll();
    }

    @Override
    @LoggableTime
    public MeterType save(MeterTypeRequest request) {
        MeterType meterType = MeterType.builder().typeName(request.typeName())
                .build();

        return meterTypeRepository.save(meterType);
    }
}
