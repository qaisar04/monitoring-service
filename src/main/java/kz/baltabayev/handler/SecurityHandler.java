package kz.baltabayev.handler;

import kz.baltabayev.ApplicationContext;
import kz.baltabayev.UserStage;
import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.out.OutputData;
import kz.baltabayev.wrapper.SecurityWrapper;

/**
 * Handler class for security-related operations. Provides methods for user registration,
 * user authorization, and related security functionalities.
 */
public class SecurityHandler {

    /**
     * Handles the user registration process, prompting for login and password,
     * registering the user, and loading the authorized user into the application context.
     *
     * @param inputData  The input data interface.
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     * @return The user stage after successful registration, typically MAIN_MENU.
     */
    public static UserStage handleRegistration(InputData inputData, OutputData outputData, MainController controller) {
        SecurityWrapper swRegister = askCredentials(inputData, outputData);
        User registeredUser = controller.register(swRegister.getLogin(), swRegister.getPassword());
        ApplicationContext.loadAuthorizePlayer(registeredUser);
        return UserStage.MAIN_MENU;
    }

    /**
     * Handles the user authorization process, prompting for login and password,
     * authorizing the user, and loading the authorized user into the application context.
     *
     * @param inputData  The input data interface.
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     * @return The user stage after successful authorization, either MAIN_MENU or ADMIN_MENU.
     */
    public static UserStage handleAuthorization(InputData inputData, OutputData outputData, MainController controller) {
        SecurityWrapper swAuthorize = askCredentials(inputData, outputData);
        User authorizedUser = controller.authorize(swAuthorize.getLogin(), swAuthorize.getPassword()).orElse(null);

        if (authorizedUser != null) {
            ApplicationContext.loadAuthorizePlayer(authorizedUser);
            return isAdmin(authorizedUser) ? UserStage.ADMIN_MENU : UserStage.MAIN_MENU;
        }

        return UserStage.MAIN_MENU;
    }

    /**
     * Prompts the user for login and password, returning a SecurityWrapper with the entered credentials.
     *
     * @param inputData  The input data interface.
     * @param outputData The output data interface.
     * @return A SecurityWrapper containing the entered login and password.
     */
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

    /**
     * Checks if the given user has the admin role.
     *
     * @param user The user to check.
     * @return True if the user has the admin role, false otherwise.
     */
    public static boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }
}
