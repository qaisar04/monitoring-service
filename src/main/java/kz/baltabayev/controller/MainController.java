package kz.baltabayev.controller;

import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.MeterType;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MainController {

    private final SecurityService securityService;
    private final MeterReadingService meterReadingService;

    public User register(String login, String password) {
        return securityService.register(login, password);
    }

    public User authorize(String login, String password) {
        return securityService.authorize(login, password);
    }

    public List<MeterReading> showCurrentMeterReadings(Long userId) {
        return meterReadingService.getCurrentMeterReadings(userId);
    }

    public void submitMeterReading(Integer counterNumber, MeterType meterType, Long userId) {
        meterReadingService.submitMeterReading(counterNumber, meterType, userId);
    }

    List<MeterReading> showMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId) {
        return meterReadingService.getMeterReadingsByMonthAndYear(year, month, userId);
    }

    public List<MeterReading> showMeterReadingHistory(Long userId) {
        return meterReadingService.getMeterReadingHistory(userId);
    }
}
