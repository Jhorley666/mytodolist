package com.bibavix.controller;

import com.bibavix.configuration.JwtUtils;
import com.bibavix.dto.*;
import com.bibavix.model.User;
import com.bibavix.repository.RoleRepository;
import com.bibavix.repository.UserRepository;
import com.bibavix.service.AuthService;
import com.bibavix.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    AuthController authController;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserDetailsServiceImpl userDetailsService;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    LoginRequest loginRequest;
    @Mock
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
    @Mock
    UserDetailsImpl userDetails;
    @Mock
    RegisterRequest registerRequest;
    @Mock
    Optional<User> optionalUser;
    @Mock
    AuthService authService;
    @Mock
    ResponseEntity<JwtResponse> jwtResponseResponseEntity;
    @Test
    void shouldReturnJwtResponseWhenAuthenticateUser() {

        try(MockedConstruction<UsernamePasswordAuthenticationToken> mockedConstruction =
                    Mockito.mockConstruction(UsernamePasswordAuthenticationToken.class, (mock, context) -> {
                        when(authenticationManager.authenticate(mock)).thenReturn(usernamePasswordAuthenticationToken);
                        when(authenticationManager.authenticate(mock).getPrincipal()).thenReturn(userDetails);
                    })) {
            JwtResponse jwtResponse = new JwtResponse();
            when(authService.authenticateUser(loginRequest)).thenReturn(ResponseEntity.ok(jwtResponse));
            ResponseEntity<JwtResponse> response = authController.authenticateUser(loginRequest);
            Assertions.assertNotNull(response);
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        }

    }

    @Test
    void shouldReturnResponseBadRequestWhenRegisterUser() {
        try(MockedConstruction<User> user =
                    Mockito.mockConstruction(User.class, (mock, context) -> {

                    })) {
            ResponseCode responseCode = new ResponseCode();
            responseCode.setHttpStatus(HttpStatus.BAD_REQUEST);
            when(authService.registerUser(registerRequest)).thenReturn(ResponseEntity.badRequest().body(responseCode));
            ResponseEntity<?> response = authController.registerUser(registerRequest);
            Assertions.assertNotNull(response);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnResponseOkWhenRegisterUser() {
        try(MockedConstruction<User> user =
                Mockito.mockConstruction(User.class, (mock, context) -> {

                })) {
            ResponseCode responseCode = new ResponseCode();
            responseCode.setHttpStatus(HttpStatus.OK);
            when(authService.registerUser(registerRequest)).thenReturn(ResponseEntity.ok(responseCode));
            ResponseEntity<?> response = authController.registerUser(registerRequest);
            Assertions.assertNotNull(response);
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

}