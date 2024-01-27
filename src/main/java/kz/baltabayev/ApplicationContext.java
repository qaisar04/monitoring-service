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
import kz.baltabayev.model.User;
import kz.baltabayev.out.ConsoleOutputData;
import kz.baltabayev.service.*;
import kz.baltabayev.service.impl.*;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static final Map<String, Object> CONTEXT = new HashMap<>();

    public static void loadContext() {
        loadDAOLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    public static void loadAuthorizePlayer(User user) {
        CONTEXT.put("authorize", user);
    }

    public static void cleanAuthorizePlayer() {
        CONTEXT.remove("authorize");
    }

    public static User getAuthorizePlayer() {
        return (User) CONTEXT.get("authorize");
    }

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
        CONTEXT.put("userDAO", new UserDAOImpl());
        CONTEXT.put("meterReadingDAO", new MeterReadingDAOImpl());
        CONTEXT.put("meterTypeDAO", new MeterTypeDAOImpl());
        CONTEXT.put("auditDAO", new AuditDAOImpl());
    }

    private static void loadServiceLayer() {
        AuditService auditService = new AuditServiceImpl((AuditDAO) CONTEXT.get("auditDAO"));
        CONTEXT.put("auditService", auditService);

        SecurityService securityService = new SecurityServiceImpl((UserDAO) CONTEXT.get("userDAO"), (AuditService) CONTEXT.get("auditService"));
        CONTEXT.put("securityService", securityService);

        UserService userService = new UserServiceImpl((UserDAO) CONTEXT.get("userDAO"));
        CONTEXT.put("userService", userService);

        MeterReadingService meterReadingService = new MeterReadingServiceImpl((MeterReadingDAO) CONTEXT.get("meterReadingDAO"), (UserService) CONTEXT.get("userService"), (AuditService) CONTEXT.get("auditService"));
        CONTEXT.put("meterReadingService", meterReadingService);

        MeterTypeService meterTypeService = new MeterTypeServiceImpl((MeterTypeDAO) CONTEXT.get("meterTypeDAO"));
        CONTEXT.put("meterTypeService", meterTypeService);
    }
}
