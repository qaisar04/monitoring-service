package kz.baltabayev.security;

import kz.baltabayev.model.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    private SecurityContext securityContext;

    public boolean isValidLogin(String login) {
        if (securityContext.getAuthentication() == null) {
            securityContext = SecurityContextHolder.getContext();
        }
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) throw new SecurityException("Unauthorized!");
        User principal = (User) authentication.getPrincipal();
        return !principal.getLogin().equals(login);
    }
}