package com.jamgm.CalTracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchItems {
    private int total;
    private int currentPage;
    private List<FoodProduct> foodProducts;
}
