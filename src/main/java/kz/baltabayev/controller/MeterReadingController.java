package kz.baltabayev.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.baltabayev.dto.MeterReadingRequest;
import kz.baltabayev.exception.AuthorizeException;
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

@RestController
@Api(value = "Meter Reading Controller", description = "Meter reading operations")
@RequestMapping("/meter-reading")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;
    private final UserRepository userRepository;

    @GetMapping("/current")
    @ApiOperation(value = "Get current meter readings", response = List.class)
    public ResponseEntity<List<MeterReading>> getCurrentMeterReadings(@RequestParam String login) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);
        return ResponseEntity.ok(meterReadingService.getCurrentMeterReadings(id));
    }

    @PostMapping("/date")
    @ApiOperation(value = "Show all meter readings by date", response = List.class)
    public ResponseEntity<?> showAllMeterReadings(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);
        List<MeterReading> meterReadingsByMonthAndYear = meterReadingService.getMeterReadingsByMonthAndYear(year, month, id);
        return ResponseEntity.ok(meterReadingsByMonthAndYear);
    }

    @GetMapping("/history")
    @ApiOperation(value = "Show meter reading history", response = List.class)
    public ResponseEntity<?> showMeterReadingHistory(
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new AuthorizeException("Incorrect login!");
        Long id = getIdByLogin(login);
        List<MeterReading> meterReadingHistory = meterReadingService.getMeterReadingHistory(id);
        return ResponseEntity.ok(meterReadingHistory);
    }

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

    private Long getIdByLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user.get().getId();
    }
}
