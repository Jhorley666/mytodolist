package com.bibavix.configuration;

import com.bibavix.dto.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private AppProperties appProperties;

    @Mock
    private AuthProperties authProperties;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private final String secret = "b1QwQ2ZzQ3h6d2p5d3J0d2Z6c2FqZ3J3d3Z6c2FqZ3J3d3Z6c2FqZ3J3d3Y=";
    private final long expirationMs = 600000L;
    private final String username = "testuser";

    @BeforeEach
    void setup() {
        Mockito.lenient().when(appProperties.getAuth()).thenReturn(authProperties);
        Mockito.lenient().when(authProperties.getTokenSecret()).thenReturn(secret);
        Mockito.lenient().when(authProperties.getTokenExpirationMs()).thenReturn(expirationMs);
    }

    @Test
    void testGenerateAndValidateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(username, "password",  Collections.emptyList());
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);

        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals(username, jwtUtils.getUsernameFromToken(token));
    }

    @Test
    void testValidateJwtToken_invalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
}