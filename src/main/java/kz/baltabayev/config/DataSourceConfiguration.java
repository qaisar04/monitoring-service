package kz.baltabayev.config;

import kz.baltabayev.util.YamlPropertySourceFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The {@code DataSourceConfiguration} class is responsible for configuring the data source used by the application.
 *
 * <p>It defines a data source bean that provides the necessary configuration for connecting to the database. Additionally, it can be
 * extended to configure database migration tools like SpringLiquibase.
 *
 * <p>Example usage:
 * <pre>
 * // Create an instance of JdbcTemplate in your Spring application.
 * &#64;Autowired
 * private JdbcTemplate jdbcTemplate;
 * </pre>
 * <p>
 * The class is annotated with `@Configuration` to indicate that it provides bean definitions.
 * It is also annotated with `@PropertySource` to specify the location of the property source (application.yml).
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class DataSourceConfiguration {

    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.driver-class-name}")
    private String driver;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${liquibase.change-log}")
    private String changeLogFile;
    @Value("${liquibase.liquibase-schema}")
    private String schemaName;

    /**
     * Creates a {@link JdbcTemplate} bean configured with the data source properties.
     *
     * @return A JdbcTemplate configured with the specified data source.
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName.split("[^\\p{L}]")[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new JdbcTemplate(dataSource);
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setLiquibaseSchema(schemaName);
        liquibase.setChangeLog(changeLogFile);
        liquibase.setDataSource(jdbcTemplate().getDataSource());
        return liquibase;
    }
}

