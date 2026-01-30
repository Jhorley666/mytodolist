package com.bibavix.service.impl;

import com.bibavix.dto.UserDetailsImpl;
import com.bibavix.model.User;
import com.bibavix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("---loadUserByUsername---");
        User user = findUserByUsername(username);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        log.info("User Found: {}", user.getUsername());
        log.info("Authorities: {}", userDetails.getAuthorities());
        return userDetails;
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username:" + username));
    }
}
