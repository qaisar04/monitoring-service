package kz.baltabayev.service.impl;

import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.exception.DuplicateRecordException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.UserService;
import kz.baltabayev.util.DateTimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kz.baltabayev.util.DateTimeUtils.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("meter reading service implementation test")
class MeterReadingServiceImplTest {

    @InjectMocks
    private MeterReadingServiceImpl meterReadingService;

    @Mock
    private MeterReadingDAO meterReadingDAO;

    @Mock
    private UserService userService;

    @Mock
    private MeterTypeService meterTypeService;

    @Test
    @DisplayName("get current meter readings method verification test")
    void getCurrentMeterReadings() {
        User mockUser = User.builder().login("testUser").build();
        lenient().when(userService.getUserById(anyLong())).thenReturn(mockUser);

        MeterReading meterReading = MeterReading.builder()
                .typeId(1L)
                .readingDate(parseDate(LocalDate.now()))
                .build();

        List<MeterReading> meterReadings = Collections.singletonList(meterReading);
        lenient().when(meterReadingDAO.findAll()).thenReturn(meterReadings);

        List<MeterReading> result = meterReadingService.getCurrentMeterReadings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(meterReading, result.get(0));
    }

    @Test
    @DisplayName("submit meter reading invalid meter type scenario test")
    void submitMeterReading_InvalidMeterType() {
        User mockUser = User.builder().login("testUser").build();
        lenient().when(userService.getUserById(anyLong())).thenReturn(mockUser);

        List<MeterType> allTypes = Collections.singletonList(MeterType.builder().id(1L).typeName("Electricity").build());
        lenient().when(meterTypeService.showAvailableMeterTypes()).thenReturn(allTypes);

        NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                () -> meterReadingService.submitMeterReading(100, 2L, 1L));

        assertEquals("Пожалуйста, введите корректный тип показаний.", exception.getMessage());
        verify(meterReadingDAO, never()).save(any());
    }

    @Test
    @DisplayName("submit meter reading duplicate record scenario test")
    void submitMeterReading_DuplicateRecord() {
        User mockUser = User.builder().login("testUser").build();
        lenient().when(userService.getUserById(anyLong())).thenReturn(mockUser);

        List<MeterType> allTypes = Collections.singletonList(MeterType.builder().id(1L).typeName("Electricity").build());
        lenient().when(meterTypeService.showAvailableMeterTypes()).thenReturn(allTypes);

        lenient().when(meterReadingDAO.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(
                MeterReading.builder()
                        .typeId(1L)
                        .readingDate(parseDate(LocalDate.now()))
                        .build()
        ));

        DuplicateRecordException exception = assertThrows(DuplicateRecordException.class,
                () -> meterReadingService.submitMeterReading(100, 1L, 1L));

        assertEquals("Запись для данного типа счетчика уже существует в текущем месяце.", exception.getMessage());
        verify(meterReadingDAO, never()).save(any());
    }

    @Test
    @DisplayName("get meter readings by month and year method verification test")
    void getMeterReadingsByMonthAndYear() {
        Long userId = -1L;

        User testUser = User.builder()
                .id(userId)
                .login("test")
                .build();

        lenient().when(userService.getUserById(userId)).thenReturn(testUser);
        lenient().when(meterReadingDAO.findAllByUserId(userId)).thenReturn(Arrays.asList(
                new MeterReading(1L, 124812409, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 1L, userId),
                new MeterReading(2L, 824123414, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 2L, userId),
                new MeterReading(3L, 249901312, DateTimeUtils.parseDate(LocalDate.of(2023, 11, 18)), 2L, userId)
        ));

        List<MeterReading> result = meterReadingService.getMeterReadingsByMonthAndYear(2024, 1, userId);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("get meter reading history method verification test")
    void getMeterReadingHistory() {
        Long userId = 1L;

        User testUser = User.builder()
                .id(userId)
                .login("test")
                .build();

        lenient().when(userService.getUserById(userId)).thenReturn(testUser);
        lenient().when(meterReadingDAO.findAllByUserId(userId)).thenReturn(Arrays.asList(
                new MeterReading(1L, 12924912, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 1L, userId),
                new MeterReading(2L, 93919112, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 2L, userId),
                new MeterReading(3L, 81238123, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 3L, userId)
        ));

        List<MeterReading> result = meterReadingService.getMeterReadingHistory(userId);

        assertEquals(3, result.size());
    }
}