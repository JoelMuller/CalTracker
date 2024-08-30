package com.jamgm.CalTracker.web.rest.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LogFoodProductDTO {
    @NotNull
    @Pattern(regexp = "\\d+", message = "barcode can only be numbers")
    private String barcode;
    @NotNull
    private LocalDate date;
    private Long userId;
}
