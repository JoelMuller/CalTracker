package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "food_items")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    private String productName;
    private String categories;
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
