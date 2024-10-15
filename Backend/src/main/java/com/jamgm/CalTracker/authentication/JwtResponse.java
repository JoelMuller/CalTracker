package com.jamgm.CalTracker.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JwtResponse {
    private final String token;
    private final String username;
    private final long userId;
    private final List<String> roles;

    public JwtResponse(String token, String username, long userId, List<String> roles) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }
}

