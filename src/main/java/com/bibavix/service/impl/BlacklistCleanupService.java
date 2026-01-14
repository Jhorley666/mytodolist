package com.bibavix.service.impl;

import com.bibavix.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class BlacklistCleanupService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Scheduled(fixedRateString = "${app.auth.tokenExpirationMs:3600000}")
    @Transactional
    public void deleteExpiredTokens() {
        log.info("Executing token blacklist cleanup");
        blacklistedTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
