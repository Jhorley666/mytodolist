package com.bibavix.controller;

import com.bibavix.dto.ResponseCode;
import com.bibavix.dto.UserTimerDTO;
import com.bibavix.model.UserTimer;
import com.bibavix.service.UserTimerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "UserTimers", description = "User Timer management APIs")
@RestController
@RequestMapping("/v1/user-timers")
@RequiredArgsConstructor
public class UserTimerController {

    private final UserTimerService userTimerService;
    private final com.bibavix.service.impl.UserDetailsServiceImpl userDetailsService;

    @PostMapping("/start")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserTimerDTO> startTimer(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        com.bibavix.model.User user = userDetailsService.findUserByUsername(userDetails.getUsername());
        UserTimerDTO userTimerDTO = userTimerService.startTimer(user.getUserId());
        return ResponseEntity.ok(userTimerDTO);
    }

    @PostMapping("/pause")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserTimerDTO> pauseTimer(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        com.bibavix.model.User user = userDetailsService.findUserByUsername(userDetails.getUsername());
        UserTimerDTO userTimerDTO = userTimerService.pauseTimer(user.getUserId());
        return ResponseEntity.ok(userTimerDTO);
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserTimerDTO> getTimerStatus(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        com.bibavix.model.User user = userDetailsService.findUserByUsername(userDetails.getUsername());
        UserTimerDTO userTimerDTO = userTimerService.getTimerStatus(user.getUserId());
        return ResponseEntity.ok(userTimerDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserTimerDTO> createUserTimer(
            @Parameter(description = "UserTimer data", required = true, schema = @Schema(implementation = UserTimer.class)) @RequestBody UserTimerDTO userTimerDTO) {
        UserTimerDTO createdUserTimer = userTimerService.createUserTimer(userTimerDTO);
        return ResponseEntity.ok(createdUserTimer);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<UserTimerDTO>> getAllUserTimers() {
        List<UserTimerDTO> userTimers = userTimerService.getAllUserTimers();
        return ResponseEntity.ok(userTimers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserTimerDTO> getUserTimerById(
            @Parameter(description = "UserTimer ID", required = true) @PathVariable UUID id) {
        UserTimerDTO userTimer = userTimerService.getUserTimerById(id);
        return ResponseEntity.ok(userTimer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserTimerDTO> updateUserTimer(
            @Parameter(description = "UserTimer ID", required = true) @PathVariable UUID id,
            @Parameter(description = "UserTimer data", required = true, schema = @Schema(implementation = UserTimer.class)) @RequestBody UserTimerDTO userTimerDTO) {
        userTimerDTO.setIdUserTimer(id);
        UserTimerDTO updatedUserTimer = userTimerService.updateUserTimer(userTimerDTO);
        return ResponseEntity.ok(updatedUserTimer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<ResponseCode> deleteUserTimer(
            @Parameter(description = "UserTimer ID", required = true) @PathVariable UUID id) {
        userTimerService.deleteUserTimerById(id);
        return ResponseEntity.ok().build();
    }
}
