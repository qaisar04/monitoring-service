package kz.baltabayev.controller;

import kz.baltabayev.dto.SecurityRequest;
import kz.baltabayev.dto.TokenResponse;
import kz.baltabayev.model.User;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/sign-up")
    public ResponseEntity<User> register(@RequestBody SecurityRequest request) {
        return ResponseEntity.ok(securityService.register(request.login(), request.password()));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> authorize(@RequestBody SecurityRequest request) {
        return ResponseEntity.ok(securityService.authorize(request.login(), request.password()));
    }
}
