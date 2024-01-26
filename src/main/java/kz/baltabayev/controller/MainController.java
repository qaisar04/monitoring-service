package kz.baltabayev.controller;

import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.SecurityService;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MainController {

    private final SecurityService securityService;
    private final MeterReadingService meterReadingService;
    private final MeterTypeService meterTypeService;
    private final UserService userService;

    public User register(String login, String password) {
        return securityService.register(login, password);
    }

    public User authorize(String login, String password) {
        return securityService.authorize(login, password);
    }

    public List<MeterReading> showCurrentMeterReadings(Long userId) {
        return meterReadingService.getCurrentMeterReadings(userId);
    }

    public void submitMeterReading(Integer counterNumber, Long meterTypeId, Long userId) {
        meterReadingService.submitMeterReading(counterNumber, meterTypeId, userId);
    }

    public List<MeterReading> showMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId) {
        return meterReadingService.getMeterReadingsByMonthAndYear(year, month, userId);
    }

    public List<MeterReading> showMeterReadingHistory(Long userId) {
        return meterReadingService.getMeterReadingHistory(userId);
    }

    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeService.showAvailableMeterTypes();
    }

    public void addNewMeterType(MeterType meterType) {
        meterTypeService.save(meterType);
    }

    public List<User> showAllUser() {
        return userService.showAllUsers();
    }



}
