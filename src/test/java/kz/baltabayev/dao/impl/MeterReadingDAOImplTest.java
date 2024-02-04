package kz.baltabayev.dao.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MeterReadingDAOImplTest extends PostgresTestContainer{

    private MeterReadingDAOImpl meterReadingDao;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        meterReadingDao = new MeterReadingDAOImpl(connectionManager);
    }

    @Test
    public void testFindById() {
        MeterReading meterReading = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(LocalDate.now())
                .build();
        meterReadingDao.save(meterReading);

        Optional<MeterReading> foundMeterReading = meterReadingDao.findById(1L);
        assertTrue(foundMeterReading.isPresent());
        assertEquals(123, foundMeterReading.get().getCounterNumber());

        Optional<MeterReading> notFoundMeterReading = meterReadingDao.findById(999L);
        assertFalse(notFoundMeterReading.isPresent());
    }

    @Test
    public void testFindAll() {
        MeterReading meterReading1 = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(LocalDate.now())
                .build();
        MeterReading meterReading2 = MeterReading.builder()
                .userId(2L)
                .typeId(2L)
                .counterNumber(456)
                .readingDate(LocalDate.now())
                .build();

        meterReadingDao.save(meterReading1);
        meterReadingDao.save(meterReading2);

        List<MeterReading> allMeterReadings = meterReadingDao.findAll();
        assertFalse(allMeterReadings.isEmpty());
        assertEquals(4, allMeterReadings.size());
    }

    @Test
    public void testSave() {
        MeterReading meterReadingToSave = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(LocalDate.now())
                .build();

        MeterReading savedMeterReading = meterReadingDao.save(meterReadingToSave);
        assertNotNull(savedMeterReading.getId());
        assertEquals(meterReadingToSave.getUserId(), savedMeterReading.getUserId());
        assertEquals(meterReadingToSave.getTypeId(), savedMeterReading.getTypeId());
        assertEquals(meterReadingToSave.getCounterNumber(), savedMeterReading.getCounterNumber());
        assertEquals(meterReadingToSave.getReadingDate(), savedMeterReading.getReadingDate());
    }

    @Test
    public void testFindAllByUserId() {
        MeterReading meterReading1 = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(LocalDate.now())
                .build();
        MeterReading meterReading2 = MeterReading.builder()
                .userId(1L)
                .typeId(2L)
                .counterNumber(456)
                .readingDate(LocalDate.now())
                .build();
        meterReadingDao.save(meterReading1);
        meterReadingDao.save(meterReading2);

        List<MeterReading> meterReadingsForUser = meterReadingDao.findAllByUserId(1L);
        assertFalse(meterReadingsForUser.isEmpty());
        assertEquals(2, meterReadingsForUser.size());
    }

    @Test
    public void testSave_NullMeterReading() {
        assertThrows(NullPointerException.class, () -> meterReadingDao.save(null));
    }
}