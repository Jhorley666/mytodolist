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

    @PostMapping("/logout")
    public ResponseEntity<ResponseCode> logoutUser(jakarta.servlet.http.HttpServletRequest request) {
        String token = parseJwt(request);
        if (token != null) {
            authService.logout(token);
        }
        return ResponseEntity.ok(new ResponseCode("Log out successful", org.springframework.http.HttpStatus.OK));
    }

    private String parseJwt(jakarta.servlet.http.HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (org.springframework.util.StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
