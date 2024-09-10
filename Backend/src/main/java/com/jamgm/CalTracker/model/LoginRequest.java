package com.jamgm.CalTracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
