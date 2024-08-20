package com.jamgm.CalTracker.web.rest.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FoodItemDTO {
    @NotNull
    private String barcode;
    @NotNull
    private String productName;
    private List<String> categories;
    @NotNull
    private String quantity;
    private Double energyKcal100g;
    private Double proteins100g;
    private Double carbohydrates100g;
    private Double sugars100g;
    private Double fat100g;
    private Double saturatedFat100g;
    private Double fiber100g;
    private Double sodium100g;
}
