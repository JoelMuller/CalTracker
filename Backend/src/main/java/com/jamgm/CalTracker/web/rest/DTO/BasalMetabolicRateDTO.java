package com.jamgm.CalTracker.web.rest.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasalMetabolicRateDTO {
    private long userId;
    @NotNull
    private double weight;
    @NotNull
    private double height;
    @NotNull
    private int age;
    private boolean male;
    private double activity;
}
