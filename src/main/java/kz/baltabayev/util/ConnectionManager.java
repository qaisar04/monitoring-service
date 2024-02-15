package kz.baltabayev.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for managing database connections using JDBC.
 */
public class ConnectionManager {
    private final String URL;
    private final String username;
    private final String password;

    public ConnectionManager(String URL, String username, String password, String driver) {
        this.URL = URL;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}