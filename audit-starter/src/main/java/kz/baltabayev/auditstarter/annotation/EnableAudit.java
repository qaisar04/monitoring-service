package kz.baltabayev.auditstarter.annotation;

import kz.baltabayev.auditstarter.config.AuditAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enable AuditAutoConfiguration class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(AuditAutoConfiguration.class)
public @interface EnableAudit {
}