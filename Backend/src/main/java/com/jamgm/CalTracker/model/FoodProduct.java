package com.jamgm.CalTracker.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
public class FoodProduct {
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
