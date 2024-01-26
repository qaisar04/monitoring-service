package kz.baltabayev.service;

import kz.baltabayev.model.Audit;

import java.util.List;

public interface AuditService {
    Audit save(Audit audit);

    List<Audit> showAllAudits();
}
