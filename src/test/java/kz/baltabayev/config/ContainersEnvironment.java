package kz.baltabayev.config;

import kz.baltabayev.containers.PostgresTestContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

}
