package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.AuditDAO;
import kz.baltabayev.model.Audit;

import java.util.*;

/**
 * Implementation of the AuditDAO interface using an in-memory map.
 * Provides methods for CRUD operations on Audit entities.
 */
public class AuditDAOImpl implements AuditDAO {

    private final Map<Long, Audit> audits = new HashMap<>();
    private Long id = 1L;

    /**
     * Retrieves an Audit entity by its ID.
     *
     * @param id The ID of the Audit entity to retrieve.
     * @return An Optional containing the found Audit entity, or an empty Optional if not found.
     */
    @Override
    public Optional<Audit> findById(Long id) {
        Audit audit = audits.get(id);
        return Optional.ofNullable(audit);
    }

    /**
     * Retrieves a list of all Audit entities.
     *
     * @return A list containing all Audit entities stored in the in-memory map.
     */
    @Override
    public List<Audit> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(audits.values()));
    }

    /**
     * Saves an Audit entity to the in-memory storage.
     *
     * @param audit The Audit entity to save.
     * @return The saved Audit entity with an assigned ID.
     */
    @Override
    public Audit save(Audit audit) {
        audit.setId(id++);
        audits.put(audit.getId(), audit);
        return audits.get(audit.getId());
    }
}
