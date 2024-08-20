package com.jamgm.CalTracker.web.rest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class OpenFoodFactsDTO {
    private String code;
    private Product product;

    @Getter
    @Setter
    public class Product {
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

        @Getter
        @Setter
        public class Nutriments{
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
            @JsonProperty("saturated-fat_100g")
            private Double saturatedFat100g;
            @JsonProperty("fiber_100g")
            private Double fiber100g;
            @JsonProperty("sodium_100g")
            private Double sodium100g;
        }
    }
}
