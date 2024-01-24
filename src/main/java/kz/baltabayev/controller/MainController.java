package kz.baltabayev.controller;

import kz.baltabayev.model.User;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
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
