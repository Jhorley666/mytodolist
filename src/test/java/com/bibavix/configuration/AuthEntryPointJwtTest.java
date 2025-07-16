package com.bibavix.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {

    @InjectMocks
    AuthEntryPointJwt authEntryPointJwt;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    AuthenticationException authException;

    @Test
    void shouldNotReturnExceptionWhenCommenceOnSendError() {
        Assertions.assertDoesNotThrow(() -> {
            authEntryPointJwt.commence(request, response, authException);
        });
    }

}