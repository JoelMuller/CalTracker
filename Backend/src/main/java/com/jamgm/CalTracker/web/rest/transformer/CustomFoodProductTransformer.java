package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.web.rest.DTO.CustomFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.NutrimentsDTO;

public class CustomFoodProductTransformer {

    public static CustomFoodProduct fromDto(CustomFoodProductDTO dto) {
        NutrimentsDTO nutriments = dto.getNutriments();

        return CustomFoodProduct.builder()
                .id(Long.parseLong(dto.get_id()))
                .product_name(dto.getProduct_name())
                .serving_size(dto.getServing_size())
                .nutriments(Nutriments.builder()
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

    public static CustomFoodProductDTO toDto(CustomFoodProduct customFoodProduct){
        Nutriments nutriments = customFoodProduct.getNutriments();

        return CustomFoodProductDTO.builder()
                ._id(Long.valueOf(customFoodProduct.getId()).toString())
                .product_name(customFoodProduct.getProduct_name())
                .serving_size(customFoodProduct.getServing_size())
                .userId(customFoodProduct.getUser().getId())
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
