package com.jamgm.CalTracker.web.rest.DTO;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LogFoodProductDTO {
    private long id;
    private Double gramsConsumed;
    @Pattern(regexp = "\\d+", message = "barcode can only be numbers")
    private String foodProductBarcode;
    private CustomFoodProductDTO customFoodProduct;
    @NotNull
    private LocalDate date;
    private Long userId;
}
