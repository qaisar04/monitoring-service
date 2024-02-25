package kz.baltabayev.repository;

import kz.baltabayev.model.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Access Object (DAO) interface for managing MeterReading entities.
 * Extends the generic MainDAO interface with specific operations for MeterReading entities.
 */
public interface MeterReadingRepository extends JpaRepository<Long, MeterReading> {
}
