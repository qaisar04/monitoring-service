package kz.baltabayev.controller;

import kz.baltabayev.dto.TokenResponse;
import kz.baltabayev.model.entity.User;
import kz.baltabayev.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityControllerTest {

    @InjectMocks
    private SecurityController securityController;

    @Mock
    private SecurityService securityService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
        }
    }

    @Test
    public void testRegister() throws Exception {
        User user = new User();
        when(securityService.register(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"test\",\"password\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthorize() throws Exception {
        String dummyToken = "dummyToken";
        TokenResponse tokenResponse = new TokenResponse(dummyToken);
        when(securityService.authorize(anyString(), anyString())).thenReturn(tokenResponse);

        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"test\",\"password\":\"test\"}"))
                .andExpect(status().isOk());
    }
}