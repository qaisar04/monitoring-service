package kz.baltabayev.handler;

import kz.baltabayev.ApplicationContext;
import kz.baltabayev.controller.MainController;
import kz.baltabayev.in.InputData;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.out.OutputData;

import java.util.List;

public class MainHandler {

    public static void handleShowCurrentMeterReagings(OutputData outputData, MainController controller) {
        List<MeterReading> meterReadings = controller.showCurrentMeterReadings(ApplicationContext.getAuthorizePlayer().getId());
        if (meterReadings.isEmpty()) {
            outputData.output("У вас нет актуальных показаний.");
        } else {
            for (MeterReading reading : meterReadings) {
                outputData.output(reading);
            }
        }
    }

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

    public static void handleViewingReadingsForSpecificMonth(InputData inputData, OutputData outputData, MainController controller) {
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

    public static void handleViewingMeterReadingHistory(OutputData outputData, MainController controller) {
        List<MeterReading> meterReadings = controller.showMeterReadingHistory(ApplicationContext.getAuthorizePlayer().getId());
        for (MeterReading reading : meterReadings) {
            outputData.output(reading);
        }
    }

    private static void showAvailableMeterTypes(OutputData outputData, MainController controller) {
        List<MeterType> meterTypes = controller.showAvailableMeterTypes();
        for (MeterType type : meterTypes) {
            outputData.output(String.format("%s. - %s", type.getId(), type.getTypeName()));
        }
    }
}
