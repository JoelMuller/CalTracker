package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.web.rest.DTO.LoggedFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.NutrimentsDTO;

public class LoggedFoodProductsTransformer {

    public static LoggedFoodProductDTO toDto(FoodProduct foodProduct, long logId, double gramsConsumed){
        Nutriments nutriments = foodProduct.getNutriments();

        return LoggedFoodProductDTO.builder()
                .id(logId)
                .product_name(foodProduct.getProduct_name())
                .serving_size(foodProduct.getServing_size())
                .gramsConsumed(gramsConsumed)
                .categories(foodProduct.getCategories() != null ? foodProduct.getCategories().toString() : "")
                .nutriments(NutrimentsDTO.builder()
                        .energyKcal100g(nutriments.getEnergyKcal100g())
                        .proteins100g(nutriments.getProteins100g())
                        .carbohydrates100g(nutriments.getCarbohydrates100g())
                        .sugars100g(nutriments.getSugars100g())
                        .fat100g(nutriments.getFat100g())
                        .saturatedFat100g(nutriments.getSaturatedFat100g())
                        .fiber100g(nutriments.getFiber100g())
                        .sodium100g(nutriments.getSodium100g())
                        .build())
                .build();
    }

    public static LoggedFoodProductDTO toDto(CustomFoodProduct customFoodProduct, long logId, double gramsConsumed){
        Nutriments nutriments = customFoodProduct.getNutriments();

        return LoggedFoodProductDTO.builder()
                .id(logId)
                .product_name(customFoodProduct.getProduct_name())
                .serving_size(customFoodProduct.getServing_size())
                .gramsConsumed(gramsConsumed)
                .nutriments(NutrimentsDTO.builder()
                        .energyKcal100g(nutriments.getEnergyKcal100g())
                        .proteins100g(nutriments.getProteins100g())
                        .carbohydrates100g(nutriments.getCarbohydrates100g())
                        .sugars100g(nutriments.getSugars100g())
                        .fat100g(nutriments.getFat100g())
                        .saturatedFat100g(nutriments.getSaturatedFat100g())
                        .fiber100g(nutriments.getFiber100g())
                        .sodium100g(nutriments.getSodium100g())
                        .build())
                .build();
    }
}
