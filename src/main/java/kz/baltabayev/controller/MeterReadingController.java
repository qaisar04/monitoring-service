package kz.baltabayev.controller;

import kz.baltabayev.repository.UserRepository;
import kz.baltabayev.dto.MeterReadingRequest;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.service.MeterReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meter-reading")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;
    private final UserRepository userRepository;
    private SecurityContext securityContext;

    @GetMapping("/current")
    public ResponseEntity<List<MeterReading>> getCurrentMeterReadings(@RequestParam String login) {
        if (!isValidLogin(login)) throw new RuntimeException("Incorrect login!"); //todo add advice

        Optional<User> user = userRepository.findByLogin(login);
        return ResponseEntity.ok(meterReadingService.getCurrentMeterReadings(user.get().getId()));
    }

    @PostMapping("/date")
    public ResponseEntity<?> showAllMeterReadings(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new RuntimeException("Incorrect login!"); //todo add advice
        Optional<User> user = userRepository.findByLogin(login);

        List<MeterReading> meterReadingsByMonthAndYear = meterReadingService.getMeterReadingsByMonthAndYear(year, month, user.get().getId());
        return ResponseEntity.ok(meterReadingsByMonthAndYear);
    }

    @GetMapping("/history")
    public ResponseEntity<?> showMeterReadingHistory(
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new RuntimeException("Incorrect login!"); //todo add advice
        Optional<User> user = userRepository.findByLogin(login);
        List<MeterReading> meterReadingHistory = meterReadingService.getMeterReadingHistory(user.get().getId());
        return ResponseEntity.ok(meterReadingHistory);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitMeterReading(
            @RequestBody MeterReadingRequest request,
            @RequestParam String login
    ) {
        if (!isValidLogin(login)) throw new RuntimeException("Incorrect login!"); //todo add advice
        Optional<User> user = userRepository.findByLogin(login);

        meterReadingService.submitMeterReading(request.counterNumber(), request.meterTypeId(), user.get().getId());
        return ResponseEntity.ok("The reading was successfully saved!");
    }

    private boolean isValidLogin(String login) {
        if (securityContext.getAuthentication() == null) securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) throw new AuthorizeException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return principal.getLogin().equals(login);
    }
}
