package kz.baltabayev.service.impl;

import kz.baltabayev.repository.AuditRepository;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link AuditService} interface.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    /**
     * Saves an audit record.
     *
     * @param audit the audit record to save
     * @return the saved audit record
     */
    public Audit save(Audit audit) {
        return auditRepository.save(audit);
    }

    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of all audit records
     */
    @Override
    public List<Audit> showAllAudits() {
        return auditRepository.findAll();
    }

    /**
     * Performs an audit for a specific action.
     *
     * @param login      the login associated with the action
     * @param actionType the type of action
     * @param auditType  the type of audit (SUCCESS or FAIL)
     * @return
     */
    @Override
    public Audit audit(String login, ActionType actionType, AuditType auditType) {
        Audit audit = Audit.builder()
                .login(login)
                .actionType(actionType)
                .auditType(auditType)
                .build();

        return save(audit);
    }
}
