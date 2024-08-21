package com.jamgm.CalTracker.web.rest.DTO;

import com.jamgm.CalTracker.model.FoodProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchItemsDTO {
    private int total;
    private int currentPage;
    private List<FoodProduct> foodProducts;
}
