package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.AuditDAO;
import kz.baltabayev.model.Audit;

import java.util.*;

public class AuditDAOImpl implements AuditDAO {

    private final Map<Long, Audit> audits = new HashMap<>();
    private Long id = 1L;

    @Override
    public Optional<Audit> findById(Long id) {
        Audit audit = audits.get(id);
        return audit == null ? Optional.empty() : Optional.of(audit);
    }

    @Override
    public List<Audit> findAll() {
        return new ArrayList<>(audits.values());
    }

    @Override
    public Audit save(Audit audit) {
        audit.setId(id);
        id++;
        audits.put(audit.getId(), audit);
        return audits.get(audit.getId());
    }
}
