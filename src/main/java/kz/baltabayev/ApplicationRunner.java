package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.User;
import kz.baltabayev.out.OutputData;

public class ApplicationRunner {

    private static MainController controller;
    private static UserStage userStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        controller = (MainController) ApplicationContext.getBean("controller");
        userStage = UserStage.SECURITY;
        outputData.output("Welcome!");

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userStage) {
                    case SECURITY -> securityProcess(inputData, outputData);

                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (RuntimeException e) {

            }
        }
        inputData.closeInput();

    }

    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String firstMessage = "Please register or log in to the application.";
        final String menu = """
                Enter one number without spaces or other symbols:
                Register - 1
                Login - 2
                Exit the application - 3
                """;

        outputData.output(firstMessage);
        while(true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.toString().equals("1")) {
                final String loginMsg = "Enter login:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();
                final String passMsg = "Enter password. The password cannot be empty and must be between 4 and 32 characters long:";
                outputData.output(passMsg);
                String password = inputData.input().toString();

                User registeredUser = controller.register(login, password);
                ApplicationContext.loadAuthorizePlayer(registeredUser);
                userStage = UserStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("2")) {
                final String loginMsg = "Enter login:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();
                final String passMsg = "Enter password:";
                outputData.output(passMsg);
                String password = inputData.input().toString();

                User authorizedUser = controller.authorize(login, password);
                ApplicationContext.loadAuthorizePlayer(authorizedUser);
                userStage = UserStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("3")) {
                userStage = UserStage.EXIT;
                break;
            } else {
                outputData.output("Unknown command, try again.");
            }
        }
    }

    private static void exitProcess(OutputData outputData) {
        final String message = "Goodbye!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
    }


    enum UserStage {
        SECURITY,
        MAIN_MENU,
        EXIT
    }
}
