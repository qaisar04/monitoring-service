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
    private AuditService auditService;

    @Mock
    private MeterTypeService meterTypeService;

    @Test
    @DisplayName("get current meter readings method verification test")
    void getCurrentMeterReadings() {
        User mockUser = User.builder().login("testUser").build();
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUser));

        MeterReading meterReading = MeterReading.builder().typeId(1L).readingDate(LocalDate.now()).build();
        List<MeterReading> meterReadings = Collections.singletonList(meterReading);
        when(meterReadingDAO.findAll()).thenReturn(meterReadings);

        List<MeterReading> result = meterReadingService.getCurrentMeterReadings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(meterReading, result.get(0));
    }

    @Test
    @DisplayName("submit meter reading success scenario test")
    void submitMeterReading_Success() {
        User mockUser = User.builder().login("testUser").build();
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUser));

        List<MeterType> allTypes = Collections.singletonList(MeterType.builder().id(1L).typeName("Electricity").build());
        when(meterTypeService.showAvailableMeterTypes()).thenReturn(allTypes);

        when(meterReadingDAO.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> meterReadingService.submitMeterReading(100, 1L, 1L));

        verify(auditService, times(1)).audit(anyString(), eq(ActionType.SUBMIT_METER), eq(AuditType.SUCCESS));
        verify(meterReadingDAO, times(1)).save(any(MeterReading.class));
    }

    @Test
    @DisplayName("submit meter reading invalid meter type scenario test")
    void submitMeterReading_InvalidMeterType() {
        User mockUser = User.builder().login("testUser").build();
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUser));

        List<MeterType> allTypes = Collections.singletonList(MeterType.builder().id(1L).typeName("Electricity").build());
        when(meterTypeService.showAvailableMeterTypes()).thenReturn(allTypes);

        NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                () -> meterReadingService.submitMeterReading(100, 2L, 1L));

        assertEquals("Пожалуйста, введите корректный тип показаний.", exception.getMessage());
        verify(auditService, times(1)).audit(anyString(), eq(ActionType.SUBMIT_METER), eq(AuditType.FAIL));
        verify(meterReadingDAO, never()).save(any());
    }

    @Test
    @DisplayName("submit meter reading duplicate record scenario test")
    void submitMeterReading_DuplicateRecord() {
        User mockUser = User.builder().login("testUser").build();
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(mockUser));

        List<MeterType> allTypes = Collections.singletonList(MeterType.builder().id(1L).typeName("Electricity").build());
        when(meterTypeService.showAvailableMeterTypes()).thenReturn(allTypes);

        when(meterReadingDAO.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(
                MeterReading.builder().typeId(1L).readingDate(LocalDate.now()).build()
        ));

        DuplicateRecordException exception = assertThrows(DuplicateRecordException.class,
                () -> meterReadingService.submitMeterReading(100, 1L, 1L));

        assertEquals("Запись для данного типа счетчика уже существует в текущем месяце.", exception.getMessage());
        verify(auditService, times(1)).audit(anyString(), eq(ActionType.SUBMIT_METER), eq(AuditType.FAIL));
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

        when(userService.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(meterReadingDAO.findAllByUserId(userId)).thenReturn(Arrays.asList(
                new MeterReading(1L, 124812409, LocalDate.of(2024, 1, 20), 1L, userId),
                new MeterReading(2L, 824123414, LocalDate.of(2024, 1, 20), 2L, userId),
                new MeterReading(3L, 249901312, LocalDate.of(2023, 11, 18), 2L, userId)
        ));

        List<MeterReading> result = meterReadingService.getMeterReadingsByMonthAndYear(2024, 1, userId);

        assertEquals(2, result.size());
        verify(auditService, times(1)).audit(eq(testUser.getLogin()), eq(ActionType.GETTING_HISTORY_OF_METER_READINGS), eq(AuditType.SUCCESS));
    }

    @Test
    @DisplayName("get meter reading history method verification test")
    void getMeterReadingHistory() {
        Long userId = 1L;

        User testUser = User.builder()
                .id(userId)
                .login("test")
                .build();

        when(userService.getUserById(userId)).thenReturn(Optional.of(testUser));
        when(meterReadingDAO.findAllByUserId(userId)).thenReturn(Arrays.asList(
                new MeterReading(1L, 12924912, LocalDate.of(2024, 1, 20), 1L, userId),
                new MeterReading(2L, 93919112, LocalDate.of(2024, 1, 20), 2L, userId),
                new MeterReading(3L, 81238123, LocalDate.of(2024, 1, 20), 3L, userId)
        ));

        List<MeterReading> result = meterReadingService.getMeterReadingHistory(userId);

        assertEquals(3, result.size());
        verify(auditService, times(1)).audit(eq(testUser.getLogin()), eq(ActionType.GETTING_HISTORY_OF_METER_READINGS), eq(AuditType.SUCCESS));
    }
}