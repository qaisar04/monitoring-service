package kz.baltabayev.service.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.model.User;
import kz.baltabayev.service.AuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private AuditService auditService;

    @Test
    void testRegister_Success() {
        String login = "login";
        String password = "password";
        User user = User.builder()
                .login(login)
                .password(password)
                .build();
        Mockito.when(userDAO.findByLogin(login)).thenReturn(Optional.empty());
        Mockito.when(userDAO.save(any(User.class))).thenReturn(user);

        User registerUser = securityService.register(login, password);
        assertEquals(login, registerUser.getLogin());
        assertEquals(password, registerUser.getPassword());
    }

    @Test
    void testRegister_ThrowException() {
        String login = "login";
        String password = "password";
        User user = User.builder()
                .login(login)
                .password(password)
                .build();
        Mockito.when(userDAO.findByLogin(login)).thenReturn(Optional.of(user));

        assertThrows(RegisterException.class, () -> securityService.register(login, password));
    }

    @Test
    void testAuthorization_Success() {
        String login = "login";
        String password = "password";
        User user = User.builder()
                .login(login)
                .password(password)
                .build();
        Mockito.when(userDAO.findByLogin(login)).thenReturn(Optional.of(user));

        User authorization = securityService.authorize(login, password);
        assertEquals(login, authorization.getLogin());
        assertEquals(password, authorization.getPassword());
    }

    @Test
    void testAuthorization_ThrowException() {
        String login = "login";
        String password = "password";
        Mockito.when(userDAO.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(AuthorizeException.class, () -> securityService.authorize(login, password));
    }
}
