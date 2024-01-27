package kz.baltabayev.handler;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.out.OutputData;

import java.util.ArrayList;
import java.util.List;

import static kz.baltabayev.handler.SecurityHandler.isAdmin;

public class AdminHandler {

    public static void handleShowListOfRegisteredParticipants(OutputData outputData, MainController controller) {
        List<User> userList = controller.showAllUser();
        List<User> adminList = new ArrayList<>();

        for (User user : userList) {
            if (isAdmin(user)) adminList.add(user);
            else {
                outputData.output(formatUser(user));
            }
        }

        for (User admin : adminList) {
            outputData.output(formatUser(admin));
        }
    }

    public static void handleShowAllAudits(OutputData outputData, MainController controller) {
        controller.showAllAudits();
    }

    public static void addNewTypeOfMeter(InputData inputData, OutputData outputData, MainController controller) {
        final String meterTypeMessage = "Введите тип счетчика:";
        outputData.output(meterTypeMessage);
        String meterType = inputData.input().toString();

        controller.addNewMeterType(
                MeterType.builder()
                        .typeName(meterType)
                        .build()
        );
    }

    private static String formatUser(User user) {
        return String.format("%s, login - %s, registration date - %s}",
                user.getId(), user.getLogin(), user.getRegistrationDate());
    }

}
