package kz.baltabayev.dao.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MeterTypeDAOImplTest extends PostgresTestContainer{

    private MeterTypeDAOImpl meterTypeDao;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        meterTypeDao = new MeterTypeDAOImpl(connectionManager);
    }

    @Test
    public void testFindById() {
        MeterType meterType = MeterType.builder()
                .typeName("TestType")
                .build();
        meterTypeDao.save(meterType);

        Optional<MeterType> foundMeterType = meterTypeDao.findById(4L);
        assertTrue(foundMeterType.isPresent());

        Optional<MeterType> notFoundMeterType = meterTypeDao.findById(999L);
        assertFalse(notFoundMeterType.isPresent());
    }

    @Test
    public void testFindAll() {
        MeterType meterType1 = MeterType.builder()
                .typeName("TestType1")
                .build();
        MeterType meterType2 = MeterType.builder()
                .typeName("TestType2")
                .build();

        meterTypeDao.save(meterType1);
        meterTypeDao.save(meterType2);

        List<MeterType> allMeterTypes = meterTypeDao.findAll();
        assertFalse(allMeterTypes.isEmpty());
        assertEquals(5, allMeterTypes.size()); // + 3 base types
    }

    @Test
    public void testSave() {
        MeterType meterTypeToSave = MeterType.builder()
                .typeName("TestType")
                .build();

        MeterType savedMeterType = meterTypeDao.save(meterTypeToSave);
        assertNotNull(savedMeterType.getId());
        assertEquals(meterTypeToSave.getTypeName(), savedMeterType.getTypeName());
    }
}