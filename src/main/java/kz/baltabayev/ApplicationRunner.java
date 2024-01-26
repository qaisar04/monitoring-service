package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.DuplicateRecordException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.out.OutputData;
import kz.baltabayev.wrapper.SecurityWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ApplicationRunner {

    private static MainController controller;
    private static UserStage userStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        controller = (MainController) ApplicationContext.getBean("controller");
        userStage = UserStage.SECURITY;

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userStage) {
                    case SECURITY -> securityProcess(inputData, outputData);
                    case MAIN_MENU -> menuProcess(inputData, outputData);
                    case ADMIN_MENU -> adminProcess(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizeException |
                     DuplicateRecordException |
                     NotValidArgumentException |
                     RegisterException e) {
                log.warn(e.getMessage());
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                outputData.errOutput("Unknown error. More details " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeInput();
    }

    private static void adminProcess(InputData inputData, OutputData outputData) {
        final String adminMessage = "Пожалуйста введите нужную вам команду.";
        final String adminMenu = """
                Введите одно число без пробелов и других символов:
                1. Получить список зарегистрированных участников.
                2. Получить весь список аудитов.
                3. Получить весь список показания.
                4. Добавить новый тип счетчика.
                5. Выйти с аккаунта.
                6. Завершить программу.
                """;

        outputData.output(adminMenu);
        while (true) {
            outputData.output(adminMessage);
            Object input = inputData.input();

            if (input.equals("1")) {
                controller.showAllUser();
            } else if (input.equals("2")) {
                // todo
                break;
            } else if (input.equals("3")) {
                break;
            } else if (input.equals("4")) {
                addNewTypeOfMeter(inputData, outputData);
                break;
            } else if (input.equals("5")) {
                userStage = UserStage.SECURITY;
                break;
            } else if (input.equals("6")) {
                userStage = UserStage.EXIT;
                break;
            } else {
                outputData.output("Введите корректную команду!");
            }


        }

    }

    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menuMessage = "Пожалуйста введите нужную вам команду.";
        final String menu = """
                Введите одно число без пробелов и других символов:
                1. Получение актуальных показаний счетчиков.
                2. Подача показаний.
                3. Просмотр показаний за конкретный месяц.
                4. Просмотр истории подачи показаний.
                5. Выйти с аккаунта.
                6. Завершить программу.
                """;

        outputData.output(menuMessage);
        while (true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.equals("1")) {
                currentMeterReagings(outputData);
            } else if (input.equals("2")) {
                submissionOfMeterReadings(inputData, outputData);
            } else if (input.equals("3")) {
                viewingReadingsForSpecificMonth(inputData, outputData);
            } else if (input.equals("4")) {
                viewingMeterReadingHistory(outputData);
            } else if (input.equals("5")) {
                userStage = UserStage.SECURITY;
                break;
            } else if (input.equals("6")) {
                userStage = UserStage.EXIT;
                break;
            } else {
                outputData.output("Введите корректную команду!");
            }
        }
    }

    private static void addNewTypeOfMeter(InputData inputData, OutputData outputData) {
        final String meterTypeMessage = "Введите тип счетчика:";
        outputData.output(meterTypeMessage);
        String meterType = inputData.input().toString();

        controller.addNewMeterType(
                MeterType.builder()
                        .typeName(meterType)
                        .build()
        );
    }

    private static void viewingMeterReadingHistory(OutputData outputData) {
        List<MeterReading> meterReadings = controller.showMeterReadingHistory(ApplicationContext.getAuthorizePlayer().getId());
        for (MeterReading reading : meterReadings) {
            outputData.output(reading);
        }
    }

    private static void viewingReadingsForSpecificMonth(InputData inputData, OutputData outputData) {
        final String yearMessage = "Введите год:";
        outputData.output(yearMessage);
        String yearOut = inputData.input().toString();
        final String monthMessage = "Введите месяц:";
        outputData.output(monthMessage);
        String monthOut = inputData.input().toString();
        List<MeterReading> meterReadings = controller.showMeterReadingsByMonthAndYear(Integer.valueOf(yearOut), Integer.valueOf(monthOut), ApplicationContext.getAuthorizePlayer().getId());
        for (MeterReading reading : meterReadings) {
            outputData.output(reading);
        }
    }

    private static void submissionOfMeterReadings(InputData inputData, OutputData outputData) {
        final String counterMess = "Введите номер счетчика:";
        outputData.output(counterMess);
        String countOutp = inputData.input().toString();

        showAvailableMeterTypes(outputData);
        String meterTypeId = inputData.input().toString();

        controller.submitMeterReading(Integer.valueOf(countOutp), Long.valueOf(meterTypeId), ApplicationContext.getAuthorizePlayer().getId());
    }

    private static void currentMeterReagings(OutputData outputData) {
        List<MeterReading> meterReadings = controller.showCurrentMeterReadings(ApplicationContext.getAuthorizePlayer().getId());
        if (meterReadings.isEmpty()) {
            outputData.output("У вас нет актуальных показаний.");
        } else {
            for (MeterReading reading : meterReadings) {
                outputData.output(reading);
            }
        }
    }

    private static void getListOfRegisteredParticipants(InputData inputData, OutputData outputData) {

    }

    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                Введите одно число без пробелов или других символов:
                1. Регистрация.
                2. Вход в систему.
                3. Завершить программу.
                """;

        while (true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.toString().equals("1")) {
                SecurityWrapper swRegister = askCredentials(inputData, outputData);
                User registeredUser = controller.register(swRegister.getLogin(), swRegister.getPassword());
                ApplicationContext.loadAuthorizePlayer(registeredUser);
                userStage = UserStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("2")) {
                SecurityWrapper swAuthorize = askCredentials(inputData, outputData);
                User authorizedUser = controller.authorize(swAuthorize.getLogin(), swAuthorize.getPassword());
                ApplicationContext.loadAuthorizePlayer(authorizedUser);
                userStage = isAdmin(authorizedUser) ? UserStage.ADMIN_MENU : UserStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("3")) {
                userStage = UserStage.EXIT;
                break;
            } else {
                outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }


    private static void exitProcess(OutputData outputData) {
        final String message = "До свидания!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
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

    private static void showAvailableMeterTypes(OutputData outputData) {
        List<MeterType> allMeterTypes = controller.showAvailableMeterTypes();

        StringBuilder meterTypeMenu = new StringBuilder("Выберите тип счетчика:\n");
        for (int i = 0; i < allMeterTypes.size(); i++) {
            meterTypeMenu.append(i + 1).append(". ").append(allMeterTypes.get(i).getTypeName()).append("\n");
        }

        outputData.output(meterTypeMenu.toString());
    }

    private static boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    enum UserStage {
        SECURITY,
        MAIN_MENU,
        ADMIN_MENU,
        EXIT
    }
}
