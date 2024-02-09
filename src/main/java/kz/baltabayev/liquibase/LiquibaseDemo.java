package kz.baltabayev.liquibase;

import kz.baltabayev.util.ConnectionManager;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The `LiquibaseDemo` class is responsible for running Liquibase database migrations. It uses Liquibase to apply
 * changesets defined in a changelog file to the connected database.
 */
@Slf4j
@AllArgsConstructor
public class LiquibaseDemo {

    /**
     * A singleton instance of the `LiquibaseDemo` class.
     */
    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    private final Connection connection;
    private final String changeLogFile;
    private final String schemaName;

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(schemaName);

            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            log.info("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}