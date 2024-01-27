package kz.baltabayev.service.impl;

import kz.baltabayev.dao.AuditDAO;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditDAO auditDAO;

    public Audit save(Audit audit) {
        return auditDAO.save(audit);
    }

    @Override
    public List<Audit> showAllAudits() {
        return auditDAO.findAll();
    }

    @Override
    public void audit(String login, ActionType actionType, AuditType auditType) {
        Audit audit = Audit.builder()
                .login(login)
                .actionType(actionType)
                .auditType(auditType)
                .build();
        save(audit);
    }
}
