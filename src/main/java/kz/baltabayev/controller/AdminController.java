package kz.baltabayev.controller;

import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.entity.MeterType;
import kz.baltabayev.model.entity.User;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles the administrative operations.
 * It allows to retrieve all users and add new meter types.
 * All endpoints in this controller require the user to have the 'ADMIN' role.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final MeterTypeService meterTypeService;

    /**
     * Retrieves a list of all users.
     * This endpoint requires the user to have the 'ADMIN' role.
     *
     * @return ResponseEntity containing a list of User objects.
     */
    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> allUsers = userService.showAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Adds a new meter type.
     * This endpoint requires the user to have the 'ADMIN' role.
     *
     * @param request The MeterTypeRequest object containing information about the new meter type.
     * @return ResponseEntity containing the saved MeterType object.
     */
    @PostMapping("/meter-type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterType> addMeterType(@RequestBody MeterTypeRequest request) {
        MeterType savedType = meterTypeService.save(request);
        return ResponseEntity.ok(savedType);
    }
}