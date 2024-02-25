package kz.baltabayev.repository;

import kz.baltabayev.model.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Access Object (DAO) interface for managing MeterType entities.
 * Extends the generic MainDAO interface with specific operations for MeterType entities.
 */
public interface MeterTypeRepository extends JpaRepository<Long, MeterType> {
}
