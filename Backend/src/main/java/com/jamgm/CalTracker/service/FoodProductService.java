package com.jamgm.CalTracker.service;

//import com.jamgm.CalTracker.repository.FoodItemRepository;
import com.jamgm.CalTracker.model.FoodProduct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FoodProductService {
    //private final FoodItemRepository foodItemRepository;
    //Searching and getting specific food items goes via openfoodfactsapi
    private final OpenFoodFactsApiService openFoodFactsApiService;

    public FoodProductService(OpenFoodFactsApiService openFoodFactsApiService){
        //this.foodItemRepository = foodItemRepository;
        this.openFoodFactsApiService = openFoodFactsApiService;
    }

    public Mono<FoodProduct> getFoodItemByBarcode(long barcode){
        return this.openFoodFactsApiService.getFoodItemByBarcode(barcode);
    }
}
