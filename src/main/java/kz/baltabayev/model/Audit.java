package kz.baltabayev.model;

import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {
    private Long id;
    private String login;
    private AuditType auditType;
    private ActionType actionType;
}
