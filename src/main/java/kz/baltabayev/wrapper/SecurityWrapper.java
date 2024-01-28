package kz.baltabayev.wrapper;

import lombok.Builder;
import lombok.Getter;

/**
 * Wrapper class for security-related information.
 */
@Getter
@Builder
public class SecurityWrapper {
    private String login;
    private String password;
}
