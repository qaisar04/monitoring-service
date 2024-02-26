package kz.baltabayev.controller;

import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.model.entity.MeterType;
import kz.baltabayev.model.entity.User;
import kz.baltabayev.service.MeterTypeService;
import kz.baltabayev.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private MeterTypeService meterTypeService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        }
    }

    @Test
    public void testShowAllUsers() throws Exception {
        List<User> users = Collections.singletonList(new User());
        when(userService.showAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/all-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddMeterType() throws Exception {
        String meterTypeName = "test";
        MeterTypeRequest request = new MeterTypeRequest(meterTypeName);
        MeterType meterType = new MeterType();
        when(meterTypeService.save(request)).thenReturn(meterType);

        mockMvc.perform(post("/admin/meter-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + meterTypeName + "\"}"))
                .andExpect(status().isOk());
    }
}