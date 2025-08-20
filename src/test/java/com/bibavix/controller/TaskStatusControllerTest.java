package com.bibavix.controller;

import com.bibavix.dto.JwtResponse;
import com.bibavix.dto.LoginRequest;
import com.bibavix.dto.RegisterRequest;
import com.bibavix.dto.TaskStatusDTO;
import com.bibavix.mytodolist.MyTodolistApplication;
import com.bibavix.service.AuthService;
import com.bibavix.service.TaskStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = MyTodolistApplication.class)
class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @MockBean
    private TaskStatusService taskStatusService;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest("azhar", "azharpass");
        RegisterRequest registerRequest = new RegisterRequest("azhar", "azhar@gmail.com", "azharpass");
        authService.registerUser(registerRequest);
        ResponseEntity<JwtResponse> jwtResponseResponseEntity = authService.authenticateUser(loginRequest);
        jwtToken = jwtResponseResponseEntity.getBody().getToken();
    }

    @Test
    void shouldReturnAllTaskStatus() throws Exception {
        List<TaskStatusDTO> taskStatusDTOList =
                Arrays.asList(
                    new TaskStatusDTO(1, "Done"),
                    new TaskStatusDTO(2, "In progress")
                );
        when(taskStatusService.getAllTaskStatus()).thenReturn(taskStatusDTOList);

        mockMvc.perform(get("/v1/status").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Done"))
                .andExpect(jsonPath("$[1].name").value("In progress"));
    }
}
