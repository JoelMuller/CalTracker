package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.FoodItem;
import com.jamgm.CalTracker.web.rest.DTO.OpenFoodFactsDTO;

public class OpenFoodFactsTransformer {
    public static FoodItem fromDto(OpenFoodFactsDTO dto) {
        if (dto == null || dto.getProduct() == null || dto.getProduct().getNutriments() == null) {
            return null; // handle null cases appropriately
        }

        // Extract the product and nutriments from the DTO
        OpenFoodFactsDTO.Product product = dto.getProduct();
        OpenFoodFactsDTO.Product.Nutriments nutriments = product.getNutriments();

        System.out.println("energy/kcal: " + nutriments.getEnergyKcal100g());
        // Build the FoodItem object using the data from the DTO
        return FoodItem.builder()
                .barcode(dto.getCode())
                .productName(product.getProduct_name())
                .categories(product.getCategories())
                .quantity(product.getServing_size())
                .energyKcal100g(nutriments.getEnergyKcal100g())
                .proteins100g(nutriments.getProteins100g())
                .carbohydrates100g(nutriments.getCarbohydrates100g())
                .sugars100g(nutriments.getSugars100g())
                .fat100g(nutriments.getFat100g())
                .saturatedFat100g(nutriments.getSaturatedFat100g())
                .fiber100g(nutriments.getFiber100g())
                .sodium100g(nutriments.getSodium100g())
                .build();
    }
}
