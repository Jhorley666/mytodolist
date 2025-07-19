package com.bibavix.service.impl;

import com.bibavix.configuration.JwtUtils;
import com.bibavix.dto.JwtResponse;
import com.bibavix.dto.LoginRequest;
import com.bibavix.dto.RegisterRequest;
import com.bibavix.dto.ResponseCode;
import com.bibavix.dto.UserDetailsImpl;
import com.bibavix.model.Role;
import com.bibavix.model.User;
import com.bibavix.repository.RoleRepository;
import com.bibavix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        Mockito.reset(authenticationManager, jwtUtils, userDetailsService, userRepository, roleRepository, passwordEncoder, authentication);
    }

    @Test
    void authenticateUser_success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("alice");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl("alice", "password", Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        ResponseEntity<JwtResponse> response = authService.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwt-token", response.getBody().getToken());
        assertEquals("alice", response.getBody().getUsername());
        assertTrue(response.getBody().getRoles().contains("ROLE_USER"));
    }

    @Test
    void registerUser_success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("bob");
        registerRequest.setPassword("pw");
        registerRequest.setEmail("bob@example.com");

        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pw")).thenReturn("hashedpw");

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<ResponseCode> response = authService.registerUser(registerRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    void registerUser_usernameTaken() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("bob");
        registerRequest.setPassword("pw");
        registerRequest.setEmail("bob@example.com");

        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(new User()));

        ResponseEntity<ResponseCode> response = authService.registerUser(registerRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username is already taken", response.getBody().getMessage());
    }
}