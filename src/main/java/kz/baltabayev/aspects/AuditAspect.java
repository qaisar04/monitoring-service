package kz.baltabayev.aspects;

import kz.baltabayev.annotations.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Pointcut("@annotation(kz.baltabayev.annotations.Audit)")
    public void auditPointcut() {
    }

    @After("auditPointcut() && @annotation(audit)")
    public void audit(JoinPoint joinPoint, Audit audit) {
        ActionType actionType = audit.actionType();
        auditService.audit(audit.login(), actionType, AuditType.SUCCESS);
    }

    @AfterThrowing(pointcut = "auditPointcut() && @annotation(audit)", throwing = "ex")
    public void auditFailure(JoinPoint joinPoint, Audit audit, Exception ex) {
        ActionType actionType = audit.actionType();
        auditService.audit(audit.login(), actionType, AuditType.FAIL);
    }
}
