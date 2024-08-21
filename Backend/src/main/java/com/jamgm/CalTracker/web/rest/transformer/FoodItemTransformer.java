package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.FoodItem;
import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.model.Product;
import com.jamgm.CalTracker.web.rest.DTO.FoodItemDTO;

public class FoodItemTransformer {
    public static FoodItem fromDto(FoodItemDTO dto) {
        if (dto == null || dto.getProduct() == null || dto.getProduct().getNutriments() == null) {
            return null; // handle null cases appropriately
        }

        // Extract the product and nutriments from the DTO
        Product product = dto.getProduct();
        Nutriments nutriments = product.getNutriments();

        // Build the FoodItem object using the data from the DTO
        return FoodItem.builder()
                .barcode(dto.getCode())
                .product(Product.builder()
                        .product_name(product.getProduct_name())
                        .categories(product.getCategories())
                        .serving_size(product.getServing_size())
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
                        .build())
                .build();
    }
}
