package kz.baltabayev;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.dao.impl.AuditDAOImpl;
import kz.baltabayev.dao.impl.MeterReadingDAOImpl;
import kz.baltabayev.dao.impl.MeterTypeDAOImpl;
import kz.baltabayev.dao.impl.UserDAOImpl;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.service.*;
import kz.baltabayev.service.impl.*;
import kz.baltabayev.util.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Application context for managing beans and dependencies.
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private ConnectionManager connectionManager;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);

        ObjectMapper jacksonMapper = new ObjectMapper();
        servletContext.setAttribute("jacksonMapper", jacksonMapper);
    }

    private void loadProperties(ServletContext servletContext) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.properties"));
                servletContext.setAttribute("servletProperties", properties);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Property file not found!");
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }

    private void databaseConfiguration(ServletContext servletContext) {
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        String dbDriver = properties.getProperty("db.driver");

        connectionManager = new ConnectionManager(dbUrl, dbUser, dbPassword);
        servletContext.setAttribute("connectionManager", connectionManager);

        String changeLogFile = properties.getProperty("liquibase.changeLogFile");
        String schemaName = properties.getProperty("liquibase.schemaName");

        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
        liquibaseDemo.runMigrations();
        servletContext.setAttribute("liquibaseDemo", liquibaseDemo);
    }

    private void serviceContextInit(ServletContext servletContext) {
        UserDAO userDAO = new UserDAOImpl(connectionManager);
        MeterTypeDAO meterTypeDAO = new MeterTypeDAOImpl(connectionManager);
        MeterReadingDAO meterReadingDAO = new MeterReadingDAOImpl(connectionManager);
        AuditDAOImpl auditDAO = new AuditDAOImpl(connectionManager);

        AuditService auditService = new AuditServiceImpl(auditDAO);
        UserService userService = new UserServiceImpl(userDAO);
        SecurityService securityService = new SecurityServiceImpl(userDAO, auditService);
        MeterTypeService meterTypeService = new MeterTypeServiceImpl(meterTypeDAO);
        MeterReadingService meterReadingService = new MeterReadingServiceImpl(meterReadingDAO, userService, auditService, meterTypeService);

        servletContext.setAttribute("auditService", auditService);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("meterTypeService", meterTypeService);
        servletContext.setAttribute("meterReadingService", meterReadingService);
    }
}
