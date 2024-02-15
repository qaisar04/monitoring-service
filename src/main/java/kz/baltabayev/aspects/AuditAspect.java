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

@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Pointcut("(within(@kz.baltabayev.annotations.Auditable *) || execution(@kz.baltabayev.annotations.Auditable * *(..))) && execution(* *(..))")
    public void annotatedByAuditable() {
    }

    @Around("annotatedByAuditable()")
    public Audit audit(ProceedingJoinPoint pjp) {
        var methodSignature = (MethodSignature) pjp.getSignature();

        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        ActionType actionType = audit.actionType();
        String payload = audit.login();
        if (payload.isEmpty()) {
            payload = audit.userId();
        }

        return auditService.audit(payload, actionType, AuditType.SUCCESS);
    }

    @AfterThrowing(pointcut = "auditPointcut() && @annotation(audit)")
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
