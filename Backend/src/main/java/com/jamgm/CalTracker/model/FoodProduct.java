package com.jamgm.CalTracker.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class FoodProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String barcode;
    private String product_name;
    @JsonSetter
    public void setCategories(Object categories) {
        if (categories instanceof String) {
            this.categories = Arrays.asList(((String) categories).split("\\s*,\\s*"));
        } else if (categories instanceof List) {
            this.categories = (List<String>) categories;
        }
    }
    public List<String> categories;
    private Nutriments nutriments;
    private String serving_size;
}
