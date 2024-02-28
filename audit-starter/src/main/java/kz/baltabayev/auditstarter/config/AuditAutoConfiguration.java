package kz.baltabayev.auditstarter.config;

import kz.baltabayev.auditstarter.aspect.AuditAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Starter configuration class audit. Class excluded from autoconfiguration.
 * To enable it you need to add the @EnableAudit annotation on configuration class.
 */
@Configuration
public class AuditAutoConfiguration {
    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect();
    }
}