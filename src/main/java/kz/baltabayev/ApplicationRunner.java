package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.MeterType;
import kz.baltabayev.out.OutputData;

import java.util.List;

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
                    case MAIN_MENU -> menuProcess(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (RuntimeException e) {
                //TODO
            }
        }
        inputData.closeInput();

    }

    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menuMessage = "Пожалуйста введите нужную вам команду.";
        final String menu = """
                Введите одно число без пробелов и других символов:
                1. Получение актуальных показаний счетчиков
                2. Подача показаний
                3. Просмотр показаний за конкретный месяц
                4. Просмотр истории подачи показаний
                """;

        final String meterType = """
                Выберите тип счетчика:
                1. HEATING
                2. COLD_WATER
                3. HOT_WATER
                """;

        outputData.output(menuMessage);
        while (true) {
            outputData.output(menu);

            Object input = inputData.input();
            if (input.equals("1")) {
                List<MeterReading> meterReadings = controller.showCurrentMeterReadings(ApplicationContext.getAuthorizePlayer().getId());
                outputData.output(meterReadings);
            } else if (input.equals("2")) {
                final String counterMess = "Enter counter:";
                outputData.output(counterMess);
                String countOutp = inputData.input().toString();
                outputData.output(meterType);
                String meterTypeString = inputData.input().toString();
                int meterTypeIndex = Integer.parseInt(meterTypeString) - 1;
                MeterType meterTypeOrig = MeterType.values()[meterTypeIndex];
                controller.submitMeterReading(Integer.valueOf(countOutp), meterTypeOrig, ApplicationContext.getAuthorizePlayer().getId());
            } else if (input.equals("3")) {
                final String yearMessage = "Enter year:";
                outputData.output(yearMessage);
                String yearOut = inputData.input().toString();
                final String monthMessage = "Enter month:";
                outputData.output(monthMessage);
                String monthOut = inputData.input().toString();
                List<MeterReading> meterReadings = controller.showMeterReadingsByMonthAndYear(Integer.valueOf(yearOut), Integer.valueOf(monthOut), ApplicationContext.getAuthorizePlayer().getId());
                outputData.output(meterReadings);
            } else if (input.equals("4")) {
                List<MeterReading> meterReadings = controller.showMeterReadingHistory(ApplicationContext.getAuthorizePlayer().getId());
                outputData.output(meterReadings);
            } else {
                outputData.output("Введите корректную команду!");
            }
        }
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
        while (true) {
            outputData.output(menu);

            Object input = inputData.input();
            if (input.toString().equals("1")) {
                final String loginMsg = "Enter login:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();

                final String passMsg = "Enter password:";
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
