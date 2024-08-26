package com.jamgm.CalTracker.web.rest.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private long id;
    @Pattern(regexp = "[a-zA-Z0-9 ]*")
    @NotNull
    private String name;
    private String email;
    @NotNull
    @Size(min = 5, message = "Password too small")
    private String password;
    private double weightLossPerWeek; //amount of kg's user wants to lose per week
}
