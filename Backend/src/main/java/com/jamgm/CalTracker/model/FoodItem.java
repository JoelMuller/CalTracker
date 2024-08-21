package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class FoodItem {
    private String barcode;
    private Product product;
}
