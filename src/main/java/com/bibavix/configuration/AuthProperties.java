package com.bibavix.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthProperties {
    private String tokenSecret;
    private long tokenExpirationMs;
}
