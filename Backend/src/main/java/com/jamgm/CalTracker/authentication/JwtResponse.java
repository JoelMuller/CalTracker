package com.jamgm.CalTracker.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {
    private final String token;
    private final String username;
    private final long userId;

    public JwtResponse(String token, String username, long userId) {
        this.token = token;
        this.username = username;
        this.userId = userId;
    }
}

