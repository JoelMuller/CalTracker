package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.model.FoodItem;
import com.jamgm.CalTracker.service.FoodItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FoodItemController {
    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService){
        this.foodItemService = foodItemService;
    }

    @GetMapping(value = "/{barcode}")
    public Mono<FoodItem> getFoodItemByBarcode(@PathVariable("barcode") final long barcode){
        return this.foodItemService.getFoodItemByBarcode(barcode);
    }
}
