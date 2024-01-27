package kz.baltabayev.controller;

import kz.baltabayev.model.Audit;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.service.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Controller class that serves as an intermediary between the application's view and various services.
 * Handles user registration, authorization, meter readings, meter types, user management, and audit log retrieval.
 */
@RequiredArgsConstructor
public class MainController {

    private final SecurityService securityService;
    private final MeterReadingService meterReadingService;
    private final MeterTypeService meterTypeService;
    private final UserService userService;
    private final AuditService auditService;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return the registered user
     */
    public User register(String login, String password) {
        return securityService.register(login, password);
    }

    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return an optional containing the authorized user, or empty if authorization fails
     */
    public Optional<User> authorize(String login, String password) {
        return securityService.authorize(login, password);
    }

    /**
     * Retrieves the current meter readings for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of current meter readings
     */
    public List<MeterReading> showCurrentMeterReadings(Long userId) {
        return meterReadingService.getCurrentMeterReadings(userId);
    }

    /**
     * Submits a new meter reading for a user with the specified counter number, meter type, and user ID.
     *
     * @param counterNumber the counter number
     * @param meterTypeId   the ID of the meter type
     * @param userId        the ID of the user
     */
    public void submitMeterReading(Integer counterNumber, Long meterTypeId, Long userId) {
        meterReadingService.submitMeterReading(counterNumber, meterTypeId, userId);
    }

    /**
     * Retrieves meter readings for a specific user for a given month and year.
     *
     * @param year  the year
     * @param month the month
     * @param userId the ID of the user
     * @return a list of meter readings for the specified month and year
     */
    public List<MeterReading> showMeterReadingsByMonthAndYear(Integer year, Integer month, Long userId) {
        return meterReadingService.getMeterReadingsByMonthAndYear(year, month, userId);
    }

    /**
     * Retrieves the meter reading history for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of meter readings representing the history
     */
    public List<MeterReading> showMeterReadingHistory(Long userId) {
        return meterReadingService.getMeterReadingHistory(userId);
    }

    /**
     * Retrieves a list of available meter types.
     *
     * @return a list of available meter types
     */
    public List<MeterType> showAvailableMeterTypes() {
        return meterTypeService.showAvailableMeterTypes();
    }

    /**
     * Adds a new meter type to the system.
     *
     * @param meterType the meter type to be added
     */
    public void addNewMeterType(MeterType meterType) {
        meterTypeService.save(meterType);
    }

    /**
     * Retrieves a list of all registered users.
     *
     * @return a list of all registered users
     */
    public List<User> showAllUser() {
        return userService.showAllUsers();
    }

    /**
     * Retrieves a list of all audit logs.
     *
     * @return a list of all audit logs
     */
    public List<Audit> showAllAudits() {
        return auditService.showAllAudits();
    }
}
