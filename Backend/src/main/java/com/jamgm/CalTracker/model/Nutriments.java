package com.jamgm.CalTracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Nutriments{
    private Double energyKcal100g;
    private Double proteins100g;
    private Double carbohydrates100g;
    private Double sugars100g;
    private Double fat100g;
    private Double saturatedFat100g;
    private Double fiber100g; //always null because fiber is not in the group nutriments in the received JSON
    private Double sodium100g;
}
