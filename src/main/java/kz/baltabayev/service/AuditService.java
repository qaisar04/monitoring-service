package kz.baltabayev.service;

import kz.baltabayev.annotations.Auditable;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;

import java.util.List;

/**
 * The service interface for auditing functionality.
 */
public interface AuditService {
    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of audit records
     */
    List<Audit> showAllAudits();

    /**
     * Performs an audit for the specified login, action type, and audit type.
     *
     * @param login      the login associated with the action
     * @param actionType the type of action being audited
     * @param auditType  the result of the audit (SUCCESS or FAIL)
     * @return
     */
    Audit audit(String login, ActionType actionType, AuditType auditType);
}
