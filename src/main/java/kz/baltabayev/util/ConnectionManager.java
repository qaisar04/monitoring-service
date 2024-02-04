package kz.baltabayev.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for managing database connections using JDBC.
 */
public class ConnectionManager {
    private final String url;
    private final String username;
    private final String password;

    public ConnectionManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}