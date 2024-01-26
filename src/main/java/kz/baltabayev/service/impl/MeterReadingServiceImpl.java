package kz.baltabayev.service.impl;

import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.exception.DuplicateRecordException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.types.MeterType;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterReadingDAO meterReadingDAO;

    @Override
    public List<MeterReading> getCurrentMeterReadings(Long userId) {
        List<MeterReading> meterReadings = meterReadingDAO.findAll();

        Map<MeterType, List<MeterReading>> readingsByType = meterReadings.stream()
                .collect(Collectors.groupingBy(MeterReading::getMeterType));

        List<MeterReading> lastReadings = new ArrayList<>();

        for (List<MeterReading> readings : readingsByType.values()) {
            if (!readings.isEmpty()) {
                MeterReading lastReading = readings.get(readings.size() - 1);
                lastReadings.add(lastReading);
            }
        }

        return lastReadings;
    }

    @Override
    public void submitMeterReading(Integer counterNumber, MeterType meterType, Long userId) {
        if (counterNumber == null || userId == null || meterType == null) {
            throw new NotValidArgumentException("Пожалуйста, заполните все пустые поля.");
        }

        LocalDateTime now = LocalDateTime.now();
        List<MeterReading> existingReadings = meterReadingDAO.findAllByUserId(userId);

        boolean alreadyExists = existingReadings.stream()
                .anyMatch(reading -> reading.getMeterType().equals(meterType) &&
                                     DateTimeUtils.isSameMonth(DateTimeUtils.parseDateTimeFromString(reading.getReadingDate()), now));

        if (alreadyExists) {
            throw new DuplicateRecordException("Запись для данного типа счетчика уже существует в текущем месяце.");
        }

        MeterReading meterReading = MeterReading.builder()
                .counterNumber(counterNumber)
                .meterType(meterType)
                .readingDate(DateTimeUtils.parseDateTime(now))
                .userId(userId)
                .build();

        meterReadingDAO.save(meterReading);
    }

    @Override
    public List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId) {
        List<MeterReading> allReadings = meterReadingDAO.findAllByUserId(userId);
        List<MeterReading> currentReadings = new ArrayList<>();
        YearMonth filterFromUser = YearMonth.of(year, month);

        for (MeterReading meterReading : allReadings) {
            LocalDateTime readingDate = DateTimeUtils.parseDateTimeFromString(meterReading.getReadingDate());
            YearMonth readingYearMonth = YearMonth.of(readingDate.getYear(), readingDate.getMonth());

            if (readingYearMonth.equals(filterFromUser)) {
                currentReadings.add(meterReading);
            }
        }

        return currentReadings;
    }

    @Override
    public List<MeterReading> getMeterReadingHistory(Long userId) {
        return meterReadingDAO.findAllByUserId(userId);
    }

    public List<MeterReading> getAllMeterReadingHistory() {
        return meterReadingDAO.findAll();
    }
}
