package kz.baltabayev.service.impl;

import kz.baltabayev.repository.UserRepository;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.model.entity.User;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("security service implementation test")
public class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("register success scenario test")
    void testRegister_Success() {
        String login = "login";
        String password = "password";
        User user = User.builder()
                .login(login)
                .password(password)
                .build();
        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User registerUser = securityService.register(login, password);
        assertEquals(login, registerUser.getLogin());
        assertEquals(password, registerUser.getPassword());
    }

    @Test
    @DisplayName("register throw exception scenario test")
    void testRegister_ThrowException() {
        String login = "login";
        String password = "password";
        User user = User.builder()
                .login(login)
                .password(password)
                .build();
        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        assertThrows(RegisterException.class, () -> securityService.register(login, password));
    }

    @Test
    @DisplayName("authorization throw exception scenario test")
    void testAuthorization_ThrowException() {
        String login = "login";
        String password = "password";
        Mockito.when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(AuthorizeException.class, () -> securityService.authorize(login, password));
    }
}
