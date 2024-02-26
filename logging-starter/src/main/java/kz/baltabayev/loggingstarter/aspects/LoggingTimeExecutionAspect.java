package kz.baltabayev.loggingstarter.aspects;

import kz.baltabayev.loggingstarter.annotations.LoggableTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

/**
 * This aspect logs the execution time of methods annotated with @LoggableTime.
 * It logs the start and end of the method execution, as well as the execution time.
 */
@Aspect
@Slf4j
public class LoggingTimeExecutionAspect {

    /**
     * Pointcut that matches methods annotated with @LoggableTime.
     */
    @Pointcut("(within(@kz.baltabayev.loggingstarter.annotations.LoggableTime *) || execution(@kz.baltabayev.loggingstarter.annotations.LoggableTime * *(..))) && execution(* *(..))")
    public void annotatedByLoggableTime() {
    }

    /**
     * Advice that wraps around the methods matched by the pointcut.
     * It logs the start and end of the method execution, as well as the execution time.
     *
     * @param pjp the proceeding join point
     * @return the result of the method execution
     * @throws Throwable if an error occurs during the method execution
     */
    @Around("annotatedByLoggableTime()")
    public Object logMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        final var stopWatch = new StopWatch();

        stopWatch.start();
        var result = pjp.proceed();
        stopWatch.stop();

        LoggableTime loggableTime = methodSignature.getMethod().getAnnotation(LoggableTime.class);
        if (loggableTime == null) {
            loggableTime = methodSignature.getMethod().getDeclaringClass().getAnnotation(LoggableTime.class);
        }

        String name = (loggableTime != null) ? loggableTime.name() : "";

        log.info(
                "%s.%s :: %s ms | %s".formatted(
                        methodSignature.getDeclaringType().getSimpleName(), // class name
                        methodSignature.getName(), // method name
                        stopWatch.getTime(TimeUnit.MILLISECONDS), // execution time in milliseconds
                        name
                )
        );

        return result;
    }
}