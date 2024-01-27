package kz.baltabayev.handler;

import kz.baltabayev.ApplicationContext;
import kz.baltabayev.UserStage;
import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.out.OutputData;
import kz.baltabayev.wrapper.SecurityWrapper;

public class SecurityHandler {

    public static UserStage handleRegistration(InputData inputData, OutputData outputData, MainController controller) {
        SecurityWrapper swRegister = askCredentials(inputData, outputData);
        User registeredUser = controller.register(swRegister.getLogin(), swRegister.getPassword());
        ApplicationContext.loadAuthorizePlayer(registeredUser);
        return UserStage.MAIN_MENU;
    }

    public static UserStage handleAuthorization(InputData inputData, OutputData outputData, MainController controller) {
        SecurityWrapper swAuthorize = askCredentials(inputData, outputData);
        User authorizedUser = controller.authorize(swAuthorize.getLogin(), swAuthorize.getPassword());
        ApplicationContext.loadAuthorizePlayer(authorizedUser);
        return isAdmin(authorizedUser) ? UserStage.ADMIN_MENU : UserStage.MAIN_MENU;
    }

    private static SecurityWrapper askCredentials(InputData inputData, OutputData outputData) {
        final String loginMsg = "Введите логин:";
        outputData.output(loginMsg);
        String login = inputData.input().toString();

        final String passMsg = "Введите пароль:";
        outputData.output(passMsg);
        String password = inputData.input().toString();

        return SecurityWrapper.builder()
                .login(login)
                .password(password)
                .build();
    }

    public static boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }
}
