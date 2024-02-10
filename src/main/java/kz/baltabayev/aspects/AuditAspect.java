package kz.baltabayev.aspects;

import kz.baltabayev.annotations.Audit;
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

    @Pointcut("(within(@kz.baltabayev.annotations.Audit *) || execution(@kz.baltabayev.annotations.Audit * *(..))) && execution(* *(..))")
    public void annotatedByAudit() {
    }

    @Around("annotatedByAudit()")
    public Audit audit(ProceedingJoinPoint pjp) {
        var methodSignature = (MethodSignature) pjp.getSignature();

        Audit audit = methodSignature.getMethod().getAnnotation(Audit.class);
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

        Audit audit = methodSignature.getMethod().getAnnotation(Audit.class);
        ActionType actionType = audit.actionType();
        String payload = audit.login();
        if (payload.isEmpty()) {
            payload = audit.userId();
        }

        auditService.audit(audit.login(), actionType, AuditType.FAIL);
    }
}
