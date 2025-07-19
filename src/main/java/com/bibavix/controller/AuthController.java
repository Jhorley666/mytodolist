package com.bibavix.controller;

import com.bibavix.dto.ResponseCode;
import com.bibavix.service.AuthService;
import com.bibavix.dto.JwtResponse;
import com.bibavix.dto.LoginRequest;
import com.bibavix.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseCode> registerUser(@RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }
}
