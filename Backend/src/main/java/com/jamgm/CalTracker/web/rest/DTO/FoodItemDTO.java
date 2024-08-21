package com.jamgm.CalTracker.web.rest.DTO;

import com.jamgm.CalTracker.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FoodItemDTO {
    private String code;
    private Product product;
}
