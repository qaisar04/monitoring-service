package kz.baltabayev.liquibase;

import kz.baltabayev.util.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The `LiquibaseDemo` class is responsible for running Liquibase database migrations. It uses Liquibase to apply
 * changesets defined in a changelog file to the connected database.
 */
@Slf4j
public class LiquibaseDemo {

    /**
     * A singleton instance of the `LiquibaseDemo` class.
     */
    private static final LiquibaseDemo liquibaseDemo = new LiquibaseDemo();
    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA);
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("migration");

            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            log.info("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of the `LiquibaseDemo` class.
     *
     * @return The singleton instance.
     */
    public static LiquibaseDemo getInstance() {
        return liquibaseDemo;
    }
}