package kz.baltabayev.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.model.User;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling admin operations.
 */
@RestController
@Api(value = "Admin Controller", description = "Admin operations")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final MeterTypeService meterTypeService;

    /**
     * Retrieves a list of all users.
     * @return ResponseEntity containing a list of User objects.
     */
    @GetMapping("/all-users")
    @ApiOperation(value = "View a list of all users", response = List.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> allUsers = userService.showAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Adds a new meter type.
     * @param request The MeterTypeRequest object containing information about the new meter type.
     * @return ResponseEntity containing the saved MeterType object.
     */
    @PostMapping("/meter-type")
    @ApiOperation(value = "Add a new meter type", response = MeterType.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterType> addMeterType(@RequestBody MeterTypeRequest request) {
        MeterType savedType = meterTypeService.save(request);
        return ResponseEntity.ok(savedType);
    }
}
