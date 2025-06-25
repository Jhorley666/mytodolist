package com.bibavix.service.impl;

import com.bibavix.model.User;
import com.bibavix.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;
    @Mock
    User user;

    @Test
    void shouldReturnUserDetailsWhenLoadUserByUsername() {
        String username = "usertest";
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Assertions.assertNotNull(userDetails);
    }

    @Test
    void shouldThrowsUsernameNotFoundExceptionWhenLoadUserByUsername() {
        String username = "usertest";
        when(userRepository.findByUsername(username)).thenThrow(new UsernameNotFoundException("User not found!"));
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(username));
    }

}