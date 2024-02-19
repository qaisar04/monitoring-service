package kz.baltabayev.repository.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static kz.baltabayev.util.DateTimeUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("meter reading dao implementation test")
public class MeterReadingRepositoryImplTest extends PostgresTestContainer {

    private MeterReadingRepositoryImpl meterReadingDao;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(), container.getUsername(), container.getPassword(),
                "org.postgresql.Driver");

        LiquibaseDemo liquibaseTest = new LiquibaseDemo(connectionManager.getConnection(), "db/changelog/changelog.xml", "migration");
        liquibaseTest.runMigrations();

        meterReadingDao = new MeterReadingRepositoryImpl(connectionManager);
    }

    @Test
    @DisplayName("find by id method verification test")
    public void testFindById() {
        MeterReading meterReading = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(parseDate(LocalDate.now()))
                .build();
        meterReadingDao.save(meterReading);

        Optional<MeterReading> foundMeterReading = meterReadingDao.findById(1L);
        assertTrue(foundMeterReading.isPresent());
        assertEquals(123, foundMeterReading.get().getCounterNumber());

        Optional<MeterReading> notFoundMeterReading = meterReadingDao.findById(999L);
        assertFalse(notFoundMeterReading.isPresent());
    }

    @Test
    @DisplayName("find all method verification test")
    public void testFindAll() {
        MeterReading meterReading1 = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(parseDate(LocalDate.now()))
                .build();
        MeterReading meterReading2 = MeterReading.builder()
                .userId(2L)
                .typeId(2L)
                .counterNumber(456)
                .readingDate(parseDate(LocalDate.now()))
                .build();

        meterReadingDao.save(meterReading1);
        meterReadingDao.save(meterReading2);

        List<MeterReading> allMeterReadings = meterReadingDao.findAll();
        assertFalse(allMeterReadings.isEmpty());
        assertEquals(4, allMeterReadings.size());
    }

    @Test
    @DisplayName("save method verification test")
    public void testSave() {
        MeterReading meterReadingToSave = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(parseDate(LocalDate.now()))
                .build();

        MeterReading savedMeterReading = meterReadingDao.save(meterReadingToSave);
        assertNotNull(savedMeterReading.getId());
        assertEquals(meterReadingToSave.getUserId(), savedMeterReading.getUserId());
        assertEquals(meterReadingToSave.getTypeId(), savedMeterReading.getTypeId());
        assertEquals(meterReadingToSave.getCounterNumber(), savedMeterReading.getCounterNumber());
        assertEquals(meterReadingToSave.getReadingDate(), savedMeterReading.getReadingDate());
    }

    @Test
    @DisplayName("find all by user id method verification test")
    public void testFindAllByUserId() {
        MeterReading meterReading1 = MeterReading.builder()
                .userId(1L)
                .typeId(1L)
                .counterNumber(123)
                .readingDate(parseDate(LocalDate.now()))
                .build();
        MeterReading meterReading2 = MeterReading.builder()
                .userId(1L)
                .typeId(2L)
                .counterNumber(456)
                .readingDate(parseDate(LocalDate.now()))
                .build();
        meterReadingDao.save(meterReading1);
        meterReadingDao.save(meterReading2);

        List<MeterReading> meterReadingsForUser = meterReadingDao.findAllByUserId(1L);
        assertFalse(meterReadingsForUser.isEmpty());
        assertEquals(2, meterReadingsForUser.size());
    }

    @Test
    @DisplayName("save null meter reading method verification test")
    public void testSave_NullMeterReading() {
        assertThrows(NullPointerException.class, () -> meterReadingDao.save(null));
    }
}