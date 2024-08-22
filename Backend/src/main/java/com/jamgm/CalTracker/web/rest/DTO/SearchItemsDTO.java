package com.jamgm.CalTracker.web.rest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamgm.CalTracker.model.FoodProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchItemsDTO {
    @JsonProperty("count")
    private int total;
    @JsonProperty("page")
    private int currentPage;
    @JsonProperty("products")
    private List<ProductDTO> foodProducts;
}
