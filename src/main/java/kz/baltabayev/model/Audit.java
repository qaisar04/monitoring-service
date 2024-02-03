package kz.baltabayev.model;

import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents an audit log entry capturing information about a user's actions.
 * Each audit entry includes a unique identifier, user login, audit type, and action type.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Audit {
    /**
     * The unique identifier for the audit entry.
     */
    Long id;
    /**
     * The login of the user associated with the audit entry.
     */
    String login;
    /**
     * The type of audit, indicating the success or failure of an action.
     */
    AuditType auditType;
    /**
     * The type of action performed by the user.
     */
    ActionType actionType;
}
