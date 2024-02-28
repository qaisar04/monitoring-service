package kz.baltabayev.service.impl;

import kz.baltabayev.repository.MeterReadingRepository;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.model.entity.MeterReading;
import kz.baltabayev.model.entity.MeterType;
import kz.baltabayev.model.entity.User;
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

import static kz.baltabayev.util.DateTimeUtils.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("meter reading service implementation test")
class MeterReadingServiceImplTest {

    @InjectMocks
    private MeterReadingServiceImpl meterReadingService;

    @Mock
    private MeterReadingRepository meterReadingRepository;

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
        lenient().when(meterReadingRepository.findAll()).thenReturn(meterReadings);

        List<MeterReading> result = meterReadingService.getCurrentMeterReadings(mockUser.getLogin());

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
                () -> meterReadingService.submitMeterReading(100, 2L, mockUser.getLogin()));

        assertEquals("Пожалуйста, введите корректный тип показаний.", exception.getMessage());
        verify(meterReadingRepository, never()).save(any());
    }

    @Test
    @DisplayName("submit meter reading duplicate record scenario test")
    void submitMeterReading_DuplicateRecord() {
        User mockUser = User.builder().login("testUser").build();
        lenient().when(userService.getUserById(anyLong())).thenReturn(mockUser);

        List<MeterType> allTypes = Collections.singletonList(MeterType.builder().id(1L).typeName("Electricity").build());
        lenient().when(meterTypeService.showAvailableMeterTypes()).thenReturn(allTypes);

        lenient().when(meterReadingRepository.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(
                MeterReading.builder()
                        .typeId(1L)
                        .readingDate(parseDate(LocalDate.now()))
                        .build()
        ));

        verify(meterReadingRepository, never()).save(any());
    }

    @Test
    @DisplayName("get meter readings by month and year method verification test")
    void getMeterReadingsByMonthAndYear() {
        Long userId = -1L;
        String login = "test";

        User testUser = User.builder()
                .id(userId)
                .login(login)
                .build();

        lenient().when(userService.getUserById(userId)).thenReturn(testUser);
        lenient().when(meterReadingRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(
                new MeterReading(1L, 124812409, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 1L, userId),
                new MeterReading(2L, 824123414, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 2L, userId),
                new MeterReading(3L, 249901312, DateTimeUtils.parseDate(LocalDate.of(2023, 11, 18)), 2L, userId)
        ));

        List<MeterReading> result = meterReadingService.getMeterReadingsByMonthAndYear(2024, 1, login);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("get meter reading history method verification test")
    void getMeterReadingHistory() {
        Long userId = 1L;
        String login = "test";

        User testUser = User.builder()
                .id(userId)
                .login(login)
                .build();

        lenient().when(userService.getUserById(userId)).thenReturn(testUser);
        lenient().when(meterReadingRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(
                new MeterReading(1L, 12924912, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 1L, userId),
                new MeterReading(2L, 93919112, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 2L, userId),
                new MeterReading(3L, 81238123, DateTimeUtils.parseDate(LocalDate.of(2024, 1, 20)), 3L, userId)
        ));

        List<MeterReading> result = meterReadingService.getMeterReadingHistory(login);

        assertEquals(3, result.size());
    }
}