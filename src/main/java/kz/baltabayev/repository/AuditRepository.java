package kz.baltabayev.repository;

import kz.baltabayev.model.Audit;

/**
 * Data Access Object (DAO) interface for managing Audit entities.
 * Extends the generic MainDAO interface with specific operations for Audit entities.
 */
public interface AuditRepository extends MainRepository<Long, Audit> {
}
