package com.jamgm.CalTracker.web.rest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDTO {
    @JsonProperty("_id")
    private String _id;
    @JsonProperty("product_name")
    private String product_name;
    @JsonProperty("categories")
    private String categories;
    private NutrimentsDTO nutriments;
    @JsonProperty("serving_size")
    private String serving_size;
}