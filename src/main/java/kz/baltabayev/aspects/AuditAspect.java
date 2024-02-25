package kz.baltabayev.aspects;

import kz.baltabayev.annotations.Auditable;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect for auditing methods annotated with @Auditable.
 */
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    /**
     * Pointcut for methods annotated with @Auditable or classes containing methods annotated with @Auditable.
     */
    @Pointcut("(within(@kz.baltabayev.annotations.Auditable *) || execution(@kz.baltabayev.annotations.Auditable * *(..))) && execution(* *(..))")
    public void annotatedByAuditable() {
    }

    /**
     * Advice to perform audit logging around methods annotated with @Auditable.
     * @param pjp The ProceedingJoinPoint representing the method being audited.
     * @return The audit information.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around("annotatedByAuditable()")
    public Audit audit(ProceedingJoinPoint pjp) throws Throwable {
        var methodSignature = (MethodSignature) pjp.getSignature();

        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        ActionType actionType = audit.actionType();
        String payload = audit.login();
        if (payload.isEmpty()) {
            payload = audit.userId();
        }

        return auditService.audit(payload, actionType, AuditType.SUCCESS);
    }

    /**
     * Advice to perform audit logging on method failure for methods annotated with @Auditable.
     * @param pjp The ProceedingJoinPoint representing the method being audited.
     */
    @AfterThrowing(pointcut = "annotatedByAuditable() && @annotation(kz.baltabayev.annotations.Auditable)")
    public void auditFailure(ProceedingJoinPoint pjp) {
        var methodSignature = (MethodSignature) pjp.getSignature();

        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        ActionType actionType = audit.actionType();
        String payload = audit.login();
        if (payload.isEmpty()) {
            payload = audit.userId();
        }

        auditService.audit(audit.login(), actionType, AuditType.FAIL);
    }
}
