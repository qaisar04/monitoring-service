package kz.baltabayev.handler;

import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.out.OutputData;

import java.util.ArrayList;
import java.util.List;

import static kz.baltabayev.handler.SecurityHandler.isAdmin;

/**
 * Handler class for administrative operations. Provides methods to handle tasks related to administrator functionalities.
 */
public class AdminHandler {

    /**
     * Displays a list of registered participants, excluding administrators, and then administrators separately.
     *
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
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

    /**
     * Displays a list of all audit entries.
     *
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
    public static void handleShowAllAudits(OutputData outputData, MainController controller) {
        List<Audit> allAudits = controller.showAllAudits();

        for (Audit audit : allAudits) {
            outputData.output(formatAudit(audit));
        }
    }

    /**
     * Adds a new type of meter based on user input.
     *
     * @param inputData  The input data interface.
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
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
        return String.format("login - %s | registration date - %s | %s",
                user.getLogin(), user.getRegistrationDate(), user.getRole());
    }

    private static String formatAudit(Audit audit) {
        return String.format("%s | %s | %s",
                audit.getLogin(), audit.getAuditType(), audit.getActionType());
    }

    /**
     * Formats the given MeterType as a string.
     *
     * @param type The MeterType to format.
     * @return The formatted string representation of the MeterType.
     */
    public static String formatMeterType(MeterType type) {
        return String.format("%s. %s",
                type.getId(), type.getTypeName());
    }
}
