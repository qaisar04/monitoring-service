package kz.baltabayev.controller;

import kz.baltabayev.model.User;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainController {

    private final SecurityService securityService;
    private final MeterReadingService meterReadingService;

    public User register(String login, String password) {
        return securityService.register(login, password);
    }

    public User authorize(String login, String password) {
        return securityService.authorize(login, password);
    }

}
