package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.dao.impl.MeterReadingDAOImpl;
import kz.baltabayev.dao.impl.UserDAOImpl;
import kz.baltabayev.in.ConsoleInputData;
import kz.baltabayev.model.User;
import kz.baltabayev.out.ConsoleOutputData;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.SecurityService;
import kz.baltabayev.service.impl.MeterReadingServiceImpl;
import kz.baltabayev.service.impl.SecurityServiceImpl;

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
                (MeterReadingService) CONTEXT.get("meterReadingService")
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
    }

    private static void loadServiceLayer() {
        SecurityService securityService = new SecurityServiceImpl((UserDAO) CONTEXT.get("userDAO"));
        MeterReadingService meterReadingService = new MeterReadingServiceImpl((MeterReadingDAO) CONTEXT.get("meterReadingDAO"));
        CONTEXT.put("securityService", securityService);
        CONTEXT.put("meterReadingService", meterReadingService);
    }
}
