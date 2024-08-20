package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.FoodItem;
import com.jamgm.CalTracker.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FoodItemService {
    private final FoodItemRepository foodItemRepository;
    //Searching and getting specific food items goes via openfoodfactsapi
    private final OpenFoodFactsApiService openFoodFactsApiService;

    public FoodItemService(FoodItemRepository foodItemRepository, OpenFoodFactsApiService openFoodFactsApiService){
        this.foodItemRepository = foodItemRepository;
        this.openFoodFactsApiService = openFoodFactsApiService;
    }

    public Mono<FoodItem> getFoodItemByBarcode(long barcode){
        return this.openFoodFactsApiService.getFoodItemByBarcode(barcode);
    }
}
