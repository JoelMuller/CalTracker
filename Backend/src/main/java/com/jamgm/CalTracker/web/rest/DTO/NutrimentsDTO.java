package com.jamgm.CalTracker.web.rest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NutrimentsDTO{
    @JsonProperty("energy-kcal_100g")
    private Double energyKcal100g;
    @JsonProperty("proteins_100g")
    private Double proteins100g;
    @JsonProperty("carbohydrates_100g")
    private Double carbohydrates100g;
    @JsonProperty("sugars_100g")
    private Double sugars100g;
    @JsonProperty("fat_100g")
    private Double fat100g;
    @JsonProperty("saturated-fat_100g") //openfoodfactsapi requirement
    private Double saturatedFat100g;
    @JsonProperty("fiber_100g")
    private Double fiber100g;
    @JsonProperty("sodium_100g")
    private Double sodium100g;
}
