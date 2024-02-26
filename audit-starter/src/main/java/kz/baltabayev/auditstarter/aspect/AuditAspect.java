package kz.baltabayev.auditstarter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * This aspect is used to audit methods annotated with @Auditable.
 * It logs the method name, signature, and arguments.
 */
@Aspect
public class AuditAspect {

    /**
     * Advice that runs before the methods matched by the pointcut.
     * It logs the method name, signature, and arguments.
     *
     * @param jp the join point
     */
    @Before("@annotation(kz.baltabayev.auditstarter.annotation.Auditable)")
    public void auditMethod(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        System.out.println("Method name " + methodName);
        System.out.println("Signature " + jp.getSignature());
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            System.out.println("Type: " + arg.getClass().toString() + "; Value: " + arg.toString());
        }
    }
}