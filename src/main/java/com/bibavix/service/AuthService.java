package com.bibavix.service;

import com.bibavix.dto.JwtResponse;
import com.bibavix.dto.LoginRequest;
import com.bibavix.dto.RegisterRequest;
import com.bibavix.dto.ResponseCode;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest);
    ResponseEntity<ResponseCode> registerUser(RegisterRequest registerRequest);
}
