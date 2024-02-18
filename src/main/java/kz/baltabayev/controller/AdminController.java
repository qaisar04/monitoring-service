package kz.baltabayev.controller;

import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final MeterTypeService meterTypeService;
    private SecurityContext securityContext;

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> showAllUsers(@RequestParam String login) {
        List<User> allUsers = userService.showAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping("/meter-type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterType> addMeterType(@RequestBody MeterTypeRequest request) {
        MeterType savedType = meterTypeService.save(request);
        return ResponseEntity.ok(savedType);
    }

    // todo
    private boolean isValidLogin(String login) {
        if (securityContext.getAuthentication() == null) securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) throw new AuthorizeException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return principal.getLogin().equals(login);
    }
}
