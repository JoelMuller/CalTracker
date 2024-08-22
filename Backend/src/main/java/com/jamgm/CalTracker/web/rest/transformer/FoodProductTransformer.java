package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.web.rest.DTO.FoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.NutrimentsDTO;
import com.jamgm.CalTracker.web.rest.DTO.ProductDTO;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FoodProductTransformer {
    public static FoodProduct fromDto(FoodProductDTO dto) {
        if (dto == null || dto.getProduct().getNutriments() == null) {
            return null;
        }
        NutrimentsDTO nutriments = dto.getProduct().getNutriments();
        if(dto.getProduct().getCategories() != null) {
            return FoodProduct.builder()
                    .barcode(dto.getProduct().get_id())
                    .product_name(dto.getProduct().getProduct_name())
                    .categories(Arrays.stream(dto.getProduct().getCategories()
                                    .split(","))
                            .map(String::trim)
                            .collect(Collectors.toList()))
                    .serving_size(dto.getProduct().getServing_size())
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
        }else{
            return FoodProduct.builder()
                    .barcode(dto.getProduct().get_id())
                    .product_name(dto.getProduct().getProduct_name())
                    .serving_size(dto.getProduct().getServing_size())
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
    }

    public static FoodProduct fromDto(ProductDTO dto) {
        if (dto == null || dto.getNutriments() == null) {
            return null;
        }
        NutrimentsDTO nutriments = dto.getNutriments();
        if(dto.getCategories() != null) {
            return FoodProduct.builder()
                    .barcode(dto.get_id())
                    .product_name(dto.getProduct_name())
                    .categories(Arrays.stream(dto.getCategories()
                                    .split(","))
                            .map(String::trim)
                            .collect(Collectors.toList()))
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
        }else{
            return FoodProduct.builder()
                    .barcode(dto.get_id())
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
    }

    public static ProductDTO toDto(FoodProduct foodProduct) {
        Nutriments nutriments = foodProduct.getNutriments();

        if(foodProduct.getCategories() != null) {
            return ProductDTO.builder()
                    ._id(foodProduct.getBarcode())
                    .product_name(foodProduct.getProduct_name())
                    .categories(String.join(",", foodProduct.getCategories()))
                    .serving_size(foodProduct.getServing_size())
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
        }else{
            return ProductDTO.builder()
                    ._id(foodProduct.getBarcode())
                    .product_name(foodProduct.getProduct_name())
                    .serving_size(foodProduct.getServing_size())
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
}
