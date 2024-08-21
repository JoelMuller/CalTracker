package com.jamgm.CalTracker.web.rest.DTO;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.Nutriments;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodProductDTO {
    private String code;
    private ProductDTO product; //Class is needed to handle the json response properly
}
