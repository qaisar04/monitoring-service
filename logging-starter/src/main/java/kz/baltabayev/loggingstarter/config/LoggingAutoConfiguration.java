package kz.baltabayev.loggingstarter.config;

import kz.baltabayev.loggingstarter.aspects.LoggingMethodExecutionAspect;
import kz.baltabayev.loggingstarter.aspects.LoggingTimeExecutionAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {

    /**
     * Bean definition for the {@code LoggingMethodExecutionAspect}.
     *
     * @return Instance of {@code LoggingMethodExecutionAspect}
     */
    @Bean
    LoggingMethodExecutionAspect loggingMethodExecutionAspect() {
        return new LoggingMethodExecutionAspect();
    }

    /**
     * Bean definition for the {@code LoggingTimeExecutionAspect}.
     *
     * @return Instance of {@code LoggingTimeExecutionAspect}
     */
    @Bean
    LoggingTimeExecutionAspect loggingTimeExecutionAspect() {
        return new LoggingTimeExecutionAspect();
    }
}

