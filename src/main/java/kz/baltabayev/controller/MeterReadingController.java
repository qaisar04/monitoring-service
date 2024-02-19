package kz.baltabayev.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.baltabayev.dto.MeterReadingRequest;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.UserNotFoundException;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.repository.UserRepository;
import kz.baltabayev.service.MeterReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static kz.baltabayev.util.SecurityUtils.isValidLogin;

/**
 * Controller class for handling meter reading operations.
 */
@RestController
@Api(value = "Meter Reading Controller", description = "Meter reading operations")
@RequestMapping("/meter-reading")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;
    private final UserRepository userRepository;

    /**
     * Retrieves current meter readings for a user.
     * @param login The user's login.
     * @return ResponseEntity containing a list of MeterReading objects.
     * @throws AuthorizeException If the login is not valid.
     */
    @GetMapping("/current")
    @ApiOperation(value = "Get current meter readings", response = List.class)
    public ResponseEntity<List<MeterReading>> getCurrentMeterReadings(@RequestParam String login) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);
        return ResponseEntity.ok(meterReadingService.getCurrentMeterReadings(id));
    }

    /**
     * Retrieves all meter readings for a user by date (year and month).
     * @param year The year.
     * @param month The month.
     * @param login The user's login.
     * @return ResponseEntity containing a list of MeterReading objects.
     * @throws AuthorizeException If the login is not valid.
     */
    @GetMapping("/date")
    @ApiOperation(value = "Show all meter readings by date", response = List.class)
    public ResponseEntity<List<MeterReading>> showAllMeterReadings(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);
        List<MeterReading> meterReadingsByMonthAndYear = meterReadingService.getMeterReadingsByMonthAndYear(year, month, id);
        return ResponseEntity.ok(meterReadingsByMonthAndYear);
    }

    /**
     * Retrieves the meter reading history for a user.
     * @param login The user's login.
     * @return ResponseEntity containing a list of MeterReading objects.
     * @throws AuthorizeException If the login is not valid.
     */
    @GetMapping("/history")
    @ApiOperation(value = "Show meter reading history", response = List.class)
    public ResponseEntity<List<MeterReading>> showMeterReadingHistory(
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);
        List<MeterReading> meterReadingHistory = meterReadingService.getMeterReadingHistory(id);
        return ResponseEntity.ok(meterReadingHistory);
    }

    /**
     * Submits a meter reading for a user.
     * @param request The MeterReadingRequest object containing information about the meter reading.
     * @param login The user's login.
     * @return ResponseEntity indicating the success of the operation.
     * @throws AuthorizeException If the login is not valid.
     */
    @PostMapping("/submit")
    @ApiOperation(value = "Submit a meter reading", response = String.class)
    public ResponseEntity<String> submitMeterReading(
            @RequestBody MeterReadingRequest request,
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);

        meterReadingService.submitMeterReading(request.counterNumber(), request.meterTypeId(), id);
        return ResponseEntity.ok("The reading was successfully saved!");
    }

    /**
     * Retrieves the user ID by login.
     * @param login The user's login.
     * @return The user's ID.
     */
    private Long getIdByLogin(String login) {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new UserNotFoundException("User not found for login: " + login);
        }
    }
}
