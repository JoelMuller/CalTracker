package com.jamgm.CalTracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Nutriments{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Double energyKcal100g;
    private Double proteins100g;
    private Double carbohydrates100g;
    private Double sugars100g;
    private Double fat100g;
    private Double saturatedFat100g;
    private Double fiber100g; //always null because fiber is not in the group nutriments in the received JSON
    private Double sodium100g;
    @OneToOne
    @JoinColumn
    private CustomFoodProduct customFoodProduct;
}
