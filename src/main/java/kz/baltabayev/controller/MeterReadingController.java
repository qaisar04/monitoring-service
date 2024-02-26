package kz.baltabayev.controller;

import kz.baltabayev.dto.MeterReadingRequest;
import kz.baltabayev.model.entity.MeterReading;
import kz.baltabayev.security.SecurityUtils;
import kz.baltabayev.service.MeterReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling meter reading operations.
 */
@RestController
@RequestMapping("/meter-reading")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    /**
     * Retrieves current meter readings for a user.
     *
     * @param login The user's login.
     * @return ResponseEntity containing a list of MeterReading objects.
     * @throws SecurityException If the login is not valid.
     */
    @GetMapping("/current")
    public ResponseEntity<List<MeterReading>> getCurrentMeterReadings(@RequestParam String login) {
        if (SecurityUtils.isValidLogin(login)) {
            throw new SecurityException("Incorrect login!");
        }
        return ResponseEntity.ok(meterReadingService.getCurrentMeterReadings(login));
    }

    /**
     * Retrieves all meter readings for a user by date (year and month).
     *
     * @param year  The year.
     * @param month The month.
     * @param login The user's login.
     * @return ResponseEntity containing a list of MeterReading objects.
     * @throws SecurityException If the login is not valid.
     */
    @GetMapping("/date")
    public ResponseEntity<List<MeterReading>> showAllMeterReadings(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String login
    ) {
        if (SecurityUtils.isValidLogin(login)) {
            throw new SecurityException("Incorrect login!");
        }
        List<MeterReading> meterReadingsByMonthAndYear = meterReadingService.getMeterReadingsByMonthAndYear(year, month, login);
        return ResponseEntity.ok(meterReadingsByMonthAndYear);
    }

    /**
     * Retrieves the meter reading history for a user.
     *
     * @param login The user's login.
     * @return ResponseEntity containing a list of MeterReading objects.
     * @throws SecurityException If the login is not valid.
     */
    @GetMapping("/history")
    public ResponseEntity<List<MeterReading>> showMeterReadingHistory(
            @RequestParam String login
    ) {
        if (SecurityUtils.isValidLogin(login)) throw new SecurityException("Incorrect login!");
        List<MeterReading> meterReadingHistory = meterReadingService.getMeterReadingHistory(login);
        return ResponseEntity.ok(meterReadingHistory);
    }

    /**
     * Submits a meter reading for a user.
     *
     * @param request The MeterReadingRequest object containing information about the meter reading.
     * @param login   The user's login.
     * @return ResponseEntity indicating the success of the operation.
     * @throws SecurityException If the login is not valid.
     */
    @PostMapping("/submit")
    public ResponseEntity<MeterReading> submitMeterReading(
            @RequestBody MeterReadingRequest request,
            @RequestParam String login
    ) {
        if (SecurityUtils.isValidLogin(login)) {
            throw new SecurityException("Incorrect login!");
        }
        MeterReading reading = meterReadingService.submitMeterReading(request.counterNumber(), request.meterTypeId(), login);
        return ResponseEntity.ok(reading);
    }
}
