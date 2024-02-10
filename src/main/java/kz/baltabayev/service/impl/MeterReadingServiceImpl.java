package kz.baltabayev.service.impl;

import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.exception.DuplicateRecordException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.UserService;
import kz.baltabayev.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link MeterReadingService} interface.
 */
@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterReadingDAO meterReadingDAO;
    private final UserService userService;
    private final AuditService auditService;
    private final MeterTypeService meterTypeService;

    /**
     * Retrieves the current meter readings for a given user.
     *
     * @param userId the ID of the user
     * @return a list of the current meter readings
     */
    @Override
    public List<MeterReading> getCurrentMeterReadings(Long userId) {
        User user = getUserByUserId(userId);

        List<MeterReading> meterReadings = meterReadingDAO.findAll();

        Map<Long, List<MeterReading>> readingsByType = meterReadings.stream()
                .collect(Collectors.groupingBy(MeterReading::getTypeId));

        List<MeterReading> lastReadings = new ArrayList<>();

        for (List<MeterReading> readings : readingsByType.values()) {
            if (!readings.isEmpty()) {
                MeterReading lastReading = readings.get(readings.size() - 1);
                lastReadings.add(lastReading);
            }
        }

        auditService.audit(
                user.getLogin(),
                ActionType.GETTING_HISTORY_OF_METER_READINGS,
                (lastReadings == null) ? AuditType.FAIL : AuditType.SUCCESS
        );

        return lastReadings;
    }

    /**
     * Submits a new meter reading for a user.
     *
     * @param counterNumber the counter number
     * @param meterTypeId   the meter type ID
     * @param userId        the user ID
     */
    @Override
    public void submitMeterReading(Integer counterNumber, Long meterTypeId, Long userId) {

        User user = getUserByUserId(userId);

        if (counterNumber == null || userId == null || meterTypeId == null) {
            auditService.audit(user.getLogin(), ActionType.SUBMIT_METER, AuditType.FAIL);
            throw new NotValidArgumentException("Пожалуйста, заполните все пустые поля.");
        }

        List<MeterType> allTypes = meterTypeService.showAvailableMeterTypes();
        if (meterTypeId > allTypes.size() || meterTypeId <= 0) {
            auditService.audit(user.getLogin(), ActionType.SUBMIT_METER, AuditType.FAIL);
            throw new NotValidArgumentException("Пожалуйста, введите корректный тип показаний.");
        }

        LocalDate now = LocalDate.now();
        List<MeterReading> existingReadings = meterReadingDAO.findAllByUserId(userId);

        boolean alreadyExists = existingReadings.stream()
                .anyMatch(reading -> reading.getTypeId().equals(meterTypeId) &&
                                     DateTimeUtils.isSameMonth(DateTimeUtils.parseDateFromString(reading.getReadingDate()), now));

        if (alreadyExists) {
            auditService.audit(user.getLogin(), ActionType.SUBMIT_METER, AuditType.FAIL);
            throw new DuplicateRecordException("Запись для данного типа счетчика уже существует в текущем месяце.");
        }

        MeterReading meterReading = MeterReading.builder()
                .counterNumber(counterNumber)
                .typeId(meterTypeId)
                .readingDate(DateTimeUtils.parseDate(LocalDate.now()))
                .userId(userId)
                .build();

        auditService.audit(user.getLogin(), ActionType.SUBMIT_METER, AuditType.SUCCESS);
        meterReadingDAO.save(meterReading);
    }

    /**
     * Retrieves meter readings for a specific month and year for a given user.
     *
     * @param year   the year
     * @param month  the month
     * @param userId the user ID
     * @return a list of meter readings for the specified month and year
     */
    @Override
    public List<MeterReading> getMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId) {
        User user = getUserByUserId(userId);

        List<MeterReading> allReadings = meterReadingDAO.findAllByUserId(userId);
        List<MeterReading> currentReadings = new ArrayList<>();
        YearMonth filterFromUser = YearMonth.of(year, month);

        for (MeterReading meterReading : allReadings) {
            LocalDate readingDate = DateTimeUtils.parseDateFromString(meterReading.getReadingDate());
            YearMonth readingYearMonth = YearMonth.of(readingDate.getYear(), readingDate.getMonth());

            if (readingYearMonth.equals(filterFromUser)) {
                currentReadings.add(meterReading);
            }
        }

        auditService.audit(
                user.getLogin(),
                ActionType.GETTING_HISTORY_OF_METER_READINGS,
                (currentReadings == null) ? AuditType.FAIL : AuditType.SUCCESS
        );

        return currentReadings;
    }

    /**
     * Retrieves the entire history of meter readings for a given user.
     *
     * @param userId the user ID
     * @return the list of all meter readings for the user
     */
    @Override
    public List<MeterReading> getMeterReadingHistory(Long userId) {
        User user = getUserByUserId(userId);
        auditService.audit(user.getLogin(), ActionType.GETTING_HISTORY_OF_METER_READINGS, AuditType.SUCCESS);
        return meterReadingDAO.findAllByUserId(userId);
    }

    /**
     * Retrieves the entire history of all meter readings.
     *
     * @return the list of all meter readings
     */
    public List<MeterReading> getAllMeterReadingHistory() {
        return meterReadingDAO.findAll();
    }

    private User getUserByUserId(Long userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new NoSuchElementException("No user found with ID: " + userId));
    }
}
