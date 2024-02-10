package kz.baltabayev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import kz.baltabayev.mapper.MeterReadingMapper;
import kz.baltabayev.mapper.UserMapper;
import kz.baltabayev.security.JwtTokenUtils;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.service.*;
import kz.baltabayev.service.impl.*;
import kz.baltabayev.util.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
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

        ObjectMapper objectMapper = new ObjectMapper();
        servletContext.setAttribute("objectMapper", objectMapper);
        servletContext.setAttribute("userMapper", UserMapper.INSTANCE);
        servletContext.setAttribute("meterReadingMapper", MeterReadingMapper.INSTANCE);
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
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        String driver = properties.getProperty("datasource.driver-class-name");

        connectionManager = new ConnectionManager(url, username, password, driver);
        servletContext.setAttribute("connectionManager", connectionManager);

        String changeLogFile = properties.getProperty("liquibase.change-log");
        String schemaName = properties.getProperty("liquibase.liquibase-schema");

        if (Boolean.parseBoolean(properties.getProperty("liquibase.enabled"))) {
            LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
            liquibaseDemo.runMigrations();
            servletContext.setAttribute("liquibaseDemo", liquibaseDemo);
        }
    }

    private void serviceContextInit(ServletContext servletContext) {
        UserDAO userDAO = new UserDAOImpl(connectionManager);
        MeterTypeDAO meterTypeDAO = new MeterTypeDAOImpl(connectionManager);
        MeterReadingDAO meterReadingDAO = new MeterReadingDAOImpl(connectionManager);
        AuditDAOImpl auditDAO = new AuditDAOImpl(connectionManager);

        AuditService auditService = new AuditServiceImpl(auditDAO);
        UserService userService = new UserServiceImpl(userDAO);

        JwtTokenUtils jwtTokenUtils = new JwtTokenUtils(
                properties.getProperty("jwt.secret"),
                Duration.parse(properties.getProperty("jwt.lifetime")),
                userService
        );

        SecurityService securityService = new SecurityServiceImpl(userDAO, auditService, jwtTokenUtils);
        MeterTypeService meterTypeService = new MeterTypeServiceImpl(meterTypeDAO);
        MeterReadingService meterReadingService = new MeterReadingServiceImpl(meterReadingDAO, userService, auditService, meterTypeService);

        servletContext.setAttribute("jwtTokenUtils", jwtTokenUtils);
        servletContext.setAttribute("auditService", auditService);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("meterTypeService", meterTypeService);
        servletContext.setAttribute("meterReadingService", meterReadingService);
    }
}
