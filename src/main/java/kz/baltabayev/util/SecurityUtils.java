package kz.baltabayev.util;

import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@UtilityClass
public class SecurityUtils {

    private SecurityContext securityContext;

    public boolean isValidLogin(String login) {
        if (securityContext.getAuthentication() == null) securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) throw new AuthorizeException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return principal.getLogin().equals(login);
    }
}