package kz.baltabayev.service;

import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;

import java.util.List;

public interface AuditService {
    List<Audit> showAllAudits();

    void audit(String login, ActionType actionType, AuditType auditType);
}
