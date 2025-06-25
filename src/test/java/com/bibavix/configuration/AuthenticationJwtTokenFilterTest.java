package com.bibavix.configuration;

import com.bibavix.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationJwtTokenFilterTest {

    @InjectMocks
    AuthenticationJwtTokenFilter authenticationJwtTokenFilter;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserDetailsServiceImpl userDetailsService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    UserDetails userDetails;

    @Test
    void shouldDoFilterInternal() throws ServletException, IOException {
        String jwt = "Bearer aei23au5sd4ak23=";
        String jwtValid = "aei23au5sd4ak23=";
        String user = "usertest";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        when(jwtUtils.validateJwtToken(jwtValid)).thenReturn(Boolean.TRUE);
        when(jwtUtils.getUsernameFromToken(jwtValid)).thenReturn(user);
        when(userDetailsService.loadUserByUsername(user)).thenReturn(userDetails);
        authenticationJwtTokenFilter.doFilterInternal(request, response, filterChain);
        verify(jwtUtils).validateJwtToken(jwtValid);
        verify(jwtUtils).getUsernameFromToken(jwtValid);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldThrowsServletExceptionWhenFilterInternal() throws ServletException, IOException {
        String jwt = "Bearer aei23au5sd4ak23=";
        String jwtValid = "aei23au5sd4ak23=";
        String user = "usertest";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        when(jwtUtils.validateJwtToken(jwtValid)).thenReturn(Boolean.TRUE);
        when(jwtUtils.getUsernameFromToken(jwtValid)).thenReturn(user);
        when(userDetailsService.loadUserByUsername(user)).thenThrow(new RuntimeException("Invalid JWT"));
        authenticationJwtTokenFilter.doFilterInternal(request, response, filterChain);
    }

    @Test
    void testDoFilterInternalWithBearerNotPresentInJwt() throws ServletException, IOException{
        String jwt = "saswqew";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        authenticationJwtTokenFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }


    @Test
    void testDoFilterInternalWithInvalidJwt() throws ServletException, IOException{
        String jwt = "Bearer aqe$32sa7sw5qe¿=";
        String invalidJwt = "aqe$32sa7sw5qe¿=";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        when(jwtUtils.validateJwtToken(invalidJwt)).thenReturn(Boolean.FALSE);
        authenticationJwtTokenFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithNullJwt() throws ServletException, IOException{
        String jwt = null;
        when(request.getHeader("Authorization")).thenReturn(jwt);
        authenticationJwtTokenFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

}