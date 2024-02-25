package kz.baltabayev.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_id_seq")
    @SequenceGenerator(name = "audit_id_seq", sequenceName = "develop.audit_id_seq", allocationSize = 1)
    Long id;
    String login;
    @Enumerated(EnumType.STRING)
    AuditType auditType;
    @Enumerated(EnumType.STRING)
    ActionType actionType;
}
