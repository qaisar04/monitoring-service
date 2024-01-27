package kz.baltabayev;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.DuplicateRecordException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.out.OutputData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static kz.baltabayev.handler.AdminHandler.*;
import static kz.baltabayev.handler.MainHandler.*;
import static kz.baltabayev.handler.SecurityHandler.handleAuthorization;
import static kz.baltabayev.handler.SecurityHandler.handleRegistration;

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
                    case SECURITY -> handleSecurity(inputData, outputData);
                    case MAIN_MENU -> handleMenu(inputData, outputData);
                    case ADMIN_MENU -> handleAdmin(inputData, outputData);
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

    public static void handleSecurity(InputData inputData, OutputData outputData) {
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
                userStage = handleRegistration(inputData, outputData, controller);
                break;
            } else if (input.toString().equals("2")) {
                userStage = handleAuthorization(inputData, outputData, controller);
                break;
            } else if (input.toString().equals("3")) {
                userStage = UserStage.EXIT;
                break;
            } else {
                outputData.output("Неизвестная команда, повторите попытку.");
            }
        }
    }

    private static void handleAdmin(InputData inputData, OutputData outputData) {
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
                handleShowListOfRegisteredParticipants(outputData, controller);
            } else if (input.equals("2")) {
                handleShowAllAudits(outputData, controller);
                break;
            } else if (input.equals("3")) {
                break;
            } else if (input.equals("4")) {
                addNewTypeOfMeter(inputData, outputData, controller);
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

    private static void handleMenu(InputData inputData, OutputData outputData) {
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
                handleShowCurrentMeterReagings(outputData, controller);
            } else if (input.equals("2")) {
                handleSubmissionOfMeterReadings(inputData, outputData, controller);
            } else if (input.equals("3")) {
                handleViewingReadingsForSpecificMonth(inputData, outputData, controller);
            } else if (input.equals("4")) {
                handleViewingMeterReadingHistory(outputData, controller);
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

    private static void exitProcess(OutputData outputData) {
        final String message = "До свидания!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
    }

    private static void showAvailableMeterTypes(OutputData outputData) {
        List<MeterType> allMeterTypes = controller.showAvailableMeterTypes();

        StringBuilder meterTypeMenu = new StringBuilder("Выберите тип счетчика:\n");
        for (int i = 0; i < allMeterTypes.size(); i++) {
            meterTypeMenu.append(i + 1).append(". ").append(allMeterTypes.get(i).getTypeName()).append("\n");
        }

        outputData.output(meterTypeMenu.toString());
    }
}
