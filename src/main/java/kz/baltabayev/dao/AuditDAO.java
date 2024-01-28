package kz.baltabayev.dao;

import kz.baltabayev.model.Audit;

/**
 * Data Access Object (DAO) interface for managing Audit entities.
 * Extends the generic MainDAO interface with specific operations for Audit entities.
 */
public interface AuditDAO extends MainDAO<Long, Audit> {
}
