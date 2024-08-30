package com.jamgm.CalTracker.web.rest.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomFoodProductDTO {
    private String _id;
    private String product_name;
    private String categories;
    private NutrimentsDTO nutriments;
    private String serving_size;
    private long userId;
}
