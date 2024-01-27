package kz.baltabayev.handler;

import kz.baltabayev.ApplicationContext;
import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.out.OutputData;

import java.util.List;

import static kz.baltabayev.handler.AdminHandler.formatMeterType;

/**
 * Handler class for main user operations. Provides methods to handle tasks related to user functionalities.
 */
public class MainHandler {

    /**
     * Displays the current meter readings for the authenticated user.
     *
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
    public static void handleShowCurrentMeterReagings(OutputData outputData, MainController controller) {
        List<MeterReading> meterReadings = controller.showCurrentMeterReadings(ApplicationContext.getAuthorizePlayer().getId());
        List<MeterType> meterTypes = controller.showAvailableMeterTypes();
        if (meterReadings.isEmpty()) outputData.output("У вас нет актуальных показаний.");
        else for (MeterReading reading : meterReadings)
            outputData.output(formatMeterReading(reading, meterTypes));
    }

    /**
     * Handles the submission of meter readings by the authenticated user.
     *
     * @param inputData  The input data interface.
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
    public static void handleSubmissionOfMeterReadings(InputData inputData, OutputData outputData, MainController controller) {
        final String counterMess = "Введите номер счетчика:";
        outputData.output(counterMess);
        String countOutp = inputData.input().toString();

        final String type = "Введите тип счетчика:";
        outputData.output(type);
        showAvailableMeterTypes(outputData, controller);
        String meterTypeId = inputData.input().toString();

        controller.submitMeterReading(Integer.valueOf(countOutp), Long.valueOf(meterTypeId), ApplicationContext.getAuthorizePlayer().getId());
    }

    /**
     * Displays meter readings for a specific month and year for the authenticated user.
     *
     * @param inputData  The input data interface.
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
    public static void handleViewingReadingsForSpecificMonth(InputData inputData, OutputData outputData, MainController controller) {
        final String yearMessage = "Введите год:";
        outputData.output(yearMessage);
        String yearOut = inputData.input().toString();
        final String monthMessage = "Введите месяц:";
        outputData.output(monthMessage);
        String monthOut = inputData.input().toString();
        List<MeterReading> meterReadings = controller.showMeterReadingsByMonthAndYear(Integer.valueOf(yearOut), Integer.valueOf(monthOut), ApplicationContext.getAuthorizePlayer().getId());
        List<MeterType> meterTypes = controller.showAvailableMeterTypes();

        for (MeterReading reading : meterReadings) {
            outputData.output(formatMeterReading(reading, meterTypes));
        }
    }

    /**
     * Displays the meter reading history for the authenticated user.
     *
     * @param outputData The output data interface.
     * @param controller The main controller for accessing business logic.
     */
    public static void handleViewingMeterReadingHistory(OutputData outputData, MainController controller) {
        List<MeterReading> meterReadings = controller.showMeterReadingHistory(ApplicationContext.getAuthorizePlayer().getId());
        for (MeterReading reading : meterReadings) {
            outputData.output(reading);
        }
    }

    private static void showAvailableMeterTypes(OutputData outputData, MainController controller) {
        List<MeterType> meterTypes = controller.showAvailableMeterTypes();
        for (MeterType type : meterTypes) {
            outputData.output(formatMeterType(type));
        }
    }

    private static String formatMeterReading(MeterReading meterReading, List<MeterType> meterTypes) {
        MeterType type = meterTypes.get(Math.toIntExact(meterReading.getTypeId() - 1));
        return String.format("%s | %s | %s",
                meterReading.getCounterNumber(), meterReading.getReadingDate(), type.getTypeName());
    }
}
