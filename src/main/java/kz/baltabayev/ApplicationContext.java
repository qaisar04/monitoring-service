package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.dao.AuditDAO;
import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.dao.MeterTypeDAO;
import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.dao.impl.AuditDAOImpl;
import kz.baltabayev.dao.impl.MeterReadingDAOImpl;
import kz.baltabayev.dao.impl.MeterTypeDAOImpl;
import kz.baltabayev.dao.impl.UserDAOImpl;
import kz.baltabayev.in.ConsoleInputData;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.User;
import kz.baltabayev.out.ConsoleOutputData;
import kz.baltabayev.service.*;
import kz.baltabayev.service.impl.*;
import kz.baltabayev.util.ConnectionManager;
import kz.baltabayev.util.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Application context for managing beans and dependencies.
 */
public class ApplicationContext {

    private static final Map<String, Object> CONTEXT = new HashMap<>();

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    /**
     * Loads the application context with DAO, Service, Controller, and Input/Output components.
     */
    public static void loadContext() {
        loadProperties();
        loadDAOLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    private static void loadProperties() {
        ConnectionManager connectionManager = new ConnectionManager(
                PropertiesUtil.get(URL_KEY),
                PropertiesUtil.get(USERNAME_KEY),
                PropertiesUtil.get(PASSWORD_KEY)
        );

        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
        liquibaseDemo.runMigrations(connectionManager.getConnection());

        CONTEXT.put("connectionManager", connectionManager);
    }

    /**
     * Loads an authorized user into the context.
     *
     * @param user The authorized user.
     */
    public static void loadAuthorizePlayer(User user) {
        CONTEXT.put("authorize", user);
    }

    /**
     * Removes the authorized user from the context.
     */
    public static void cleanAuthorizePlayer() {
        CONTEXT.remove("authorize");
    }

    /**
     * Retrieves the authorized user from the context.
     *
     * @return The authorized user.
     */
    public static User getAuthorizePlayer() {
        return (User) CONTEXT.get("authorize");
    }

    /**
     * Retrieves a bean from the context by its name.
     *
     * @param beanName The name of the bean.
     * @return The bean object.
     */
    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadControllers() {
        MainController controller = new MainController(
                (SecurityService) CONTEXT.get("securityService"),
                (MeterReadingService) CONTEXT.get("meterReadingService"),
                (MeterTypeService) CONTEXT.get("meterTypeService"),
                (UserService) CONTEXT.get("userService"),
                (AuditService) CONTEXT.get("auditService")
        );
        CONTEXT.put("controller", controller);
    }

    private static void loadInputOutputLayer() {
        CONTEXT.put("input", new ConsoleInputData());
        CONTEXT.put("output", new ConsoleOutputData());
    }

    private static void loadDAOLayer() {
        CONTEXT.put("userDAO", new UserDAOImpl(
                (ConnectionManager) CONTEXT.get("connectionManager")
        ));
        CONTEXT.put("meterReadingDAO", new MeterReadingDAOImpl(
                (ConnectionManager) CONTEXT.get("connectionManager")
        ));
        CONTEXT.put("meterTypeDAO", new MeterTypeDAOImpl(
                (ConnectionManager) CONTEXT.get("connectionManager")
        ));
        CONTEXT.put("auditDAO", new AuditDAOImpl(
                (ConnectionManager) CONTEXT.get("connectionManager")
        ));
    }

    private static void loadServiceLayer() {
        AuditService auditService = new AuditServiceImpl((AuditDAO) CONTEXT.get("auditDAO"));
        CONTEXT.put("auditService", auditService);

        SecurityService securityService = new SecurityServiceImpl(
                (UserDAO) CONTEXT.get("userDAO"),
                (AuditService) CONTEXT.get("auditService")
        );
        CONTEXT.put("securityService", securityService);

        UserService userService = new UserServiceImpl((UserDAO) CONTEXT.get("userDAO"));
        CONTEXT.put("userService", userService);

        MeterTypeService meterTypeService = new MeterTypeServiceImpl((MeterTypeDAO) CONTEXT.get("meterTypeDAO"));
        CONTEXT.put("meterTypeService", meterTypeService);

        MeterReadingService meterReadingService = new MeterReadingServiceImpl(
                (MeterReadingDAO) CONTEXT.get("meterReadingDAO"),
                (UserService) CONTEXT.get("userService"),
                (AuditService) CONTEXT.get("auditService"),
                (MeterTypeService) CONTEXT.get("meterTypeService")
        );
        CONTEXT.put("meterReadingService", meterReadingService);
    }
}
