package kz.baltabayev.repository;

import kz.baltabayev.model.entity.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object (DAO) interface for managing MeterType entities.
 * Extends the generic MainDAO interface with specific operations for MeterType entities.
 */
@Repository
public interface MeterTypeRepository extends JpaRepository<MeterType, Long> {
}
