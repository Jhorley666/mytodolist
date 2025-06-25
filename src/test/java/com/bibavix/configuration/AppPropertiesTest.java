package com.bibavix.configuration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppPropertiesTest {

    @InjectMocks
    AppProperties appProperties;

    @Mock
    private AuthProperties authProperties;

    @Test
    void shouldReturnNullWhenGetAuthProperties() {
        appProperties.setAuth(null);
        AuthProperties auth = appProperties.getAuth();
        Assertions.assertNull(auth);
    }

    @Test
    void testGetAuthWhenSet() {
        String secretToken = "as7da02ad#aAl$-=asd";
        long tokenExpirationMs = 360000L;
        // Arrange: Mock AuthProperties behavior
        when(authProperties.getTokenSecret()).thenReturn(secretToken);
        when(authProperties.getTokenExpirationMs()).thenReturn(tokenExpirationMs);
        appProperties.setAuth(authProperties);

        // Act: Call the getter
        AuthProperties result = appProperties.getAuth();

        // Assert: Verify the getter returns the mocked AuthProperties
        assertNotNull(result, "AuthProperties should not be null");
        assertEquals(secretToken, result.getTokenSecret(), "Username should match mocked value");
        assertEquals(tokenExpirationMs, result.getTokenExpirationMs(), "Token should match mocked value");
    }

    @Test
    void testGetAuthWhenNotSet() {
        // Arrange: Do not set auth field
        appProperties.setAuth(null);

        // Act: Call the getter
        AuthProperties result = appProperties.getAuth();

        // Assert: Verify the getter returns null
        assertNull(result, "AuthProperties should be null when not set");
    }

    @Test
    void setAuth() {
        String secretToken = "as7da02ad#aAl$-=asd";
        long tokenExpirationMs = 360000L;
        AuthProperties authProperties1 = new AuthProperties();
        authProperties1.setTokenSecret(secretToken);
        authProperties1.setTokenExpirationMs(tokenExpirationMs);
        appProperties.setAuth(authProperties1);
        Assertions.assertEquals(secretToken, appProperties.getAuth().getTokenSecret());
        Assertions.assertEquals(tokenExpirationMs, appProperties.getAuth().getTokenExpirationMs());
    }
}