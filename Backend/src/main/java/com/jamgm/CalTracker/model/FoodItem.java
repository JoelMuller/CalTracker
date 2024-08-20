package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@Table(name = "food_items")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String barcode;
    private String productName;
    private List<String> categories; //categories that can be used to describe the food or drink
    private String quantity; //amount of grams in one serving
    //nutrition information per 100g
    private Double energyKcal100g;
    private Double proteins100g;
    private Double carbohydrates100g;
    private Double sugars100g;
    private Double fat100g;
    private Double saturatedFat100g;
    private Double fiber100g;
    private Double sodium100g;

}
