package kz.baltabayev.repository;

import kz.baltabayev.model.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object (DAO) interface for managing MeterReading entities.
 * Extends the generic MainDAO interface with specific operations for MeterReading entities.
 */
@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findAllByUserId(Long userId);
}
