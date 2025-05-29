package com.bibavix.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for REST APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/tasks/**").authenticated()
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .httpBasic(basic -> {})
            .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String userTest = System.getenv("USER_TEST");
        String passwordTest = System.getenv("PASSWORD_TEST");
        var user = User.withUsername(userTest).password(passwordTest).roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }
}