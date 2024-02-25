package kz.baltabayev.controller;

import kz.baltabayev.dto.MeterReadingRequest;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.repository.UserRepository;
import kz.baltabayev.service.MeterReadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MeterReadingControllerTest {

    @Mock
    private MeterReadingService meterReadingService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MeterReadingController meterReadingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(meterReadingController).build();

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetCurrentMeterReadings() throws Exception {
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(new User()));
        when(meterReadingService.getCurrentMeterReadings(anyLong())).thenReturn(Arrays.asList(new MeterReading()));

        mockMvc.perform(get("/meter-reading/current")
                        .param("login", "testLogin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSubmitMeterReading() throws Exception {
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(new User()));
        doNothing().when(meterReadingService).submitMeterReading(any(), any(), anyLong());

        mockMvc.perform(post("/meter-reading/submit")
                        .param("login", "testLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"counterNumber\": \"123\", \"meterTypeId\": \"1\"}"))
                .andExpect(status().isOk());
    }
}