package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.web.rest.DTO.SearchItemsDTO;

import java.util.stream.Collectors;

public class SearchItemsTransformer {
    public static SearchItems fromDto(SearchItemsDTO dto){
        return SearchItems.builder()
                .total(dto.getTotal())
                .currentPage(dto.getCurrentPage())
                .foodProducts(dto.getFoodProducts()
                        .stream()
                        .map(FoodProductTransformer::fromDto)
                        .collect(Collectors.toList()))
                .build();
    }
    public static SearchItemsDTO toDto(SearchItems searchItems){
        return SearchItemsDTO.builder()
                .total(searchItems.getTotal())
                .currentPage(searchItems.getCurrentPage())
                .foodProducts(searchItems.getFoodProducts()
                        .stream()
                        .map(FoodProductTransformer::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
