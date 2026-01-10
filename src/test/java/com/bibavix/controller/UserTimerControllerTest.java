package com.bibavix.controller;

import com.bibavix.dto.UserTimerDTO;
import com.bibavix.model.User;
import com.bibavix.service.UserTimerService;
import com.bibavix.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserTimerControllerTest {

    @Mock
    private UserTimerService userTimerService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserTimerController userTimerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTimerStatus_ReturnsStatusForAuthenticatedUser() {
        // Arrange
        String username = "testuser";
        Integer userId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);

        UserTimerDTO expectedDto = new UserTimerDTO();
        expectedDto.setUserId(userId);
        expectedDto.setRemainingSeconds(100L);

        when(userDetails.getUsername()).thenReturn(username);
        when(userDetailsService.findUserByUsername(username)).thenReturn(user);
        when(userTimerService.getTimerStatus(userId)).thenReturn(expectedDto);

        // Act
        ResponseEntity<UserTimerDTO> response = userTimerController.getTimerStatus(userDetails);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedDto, response.getBody());
        verify(userDetailsService).findUserByUsername(username);
        verify(userTimerService).getTimerStatus(userId);
    }
}
