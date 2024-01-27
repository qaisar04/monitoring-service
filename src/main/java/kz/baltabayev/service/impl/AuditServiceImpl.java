package kz.baltabayev.service.impl;

import kz.baltabayev.dao.AuditDAO;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementation of the {@link AuditService} interface.
 */
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditDAO auditDAO;

    /**
     * Saves an audit record.
     *
     * @param audit the audit record to save
     * @return the saved audit record
     */
    public Audit save(Audit audit) {
        return auditDAO.save(audit);
    }

    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of all audit records
     */
    @Override
    public List<Audit> showAllAudits() {
        return auditDAO.findAll();
    }

    /**
     * Performs an audit for a specific action.
     *
     * @param login     the login associated with the action
     * @param actionType the type of action
     * @param auditType  the type of audit (SUCCESS or FAIL)
     */
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
