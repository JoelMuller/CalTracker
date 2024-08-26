package com.jamgm.CalTracker.service;

//import com.jamgm.CalTracker.repository.FoodItemRepository;
import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.LogFoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.repository.LogFoodProductRepository;
import com.jamgm.CalTracker.web.rest.DTO.LogFoodProductDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class FoodProductService {
    //private final FoodItemRepository foodItemRepository;

    private final LogFoodProductRepository logFoodProductRepository;
    //Searching and getting specific food items goes via openfoodfactsapi
    private final OpenFoodFactsApiService openFoodFactsApiService;

    public FoodProductService(OpenFoodFactsApiService openFoodFactsApiService, LogFoodProductRepository logFoodProductRepository){
        //this.foodItemRepository = foodItemRepository;
        this.logFoodProductRepository = logFoodProductRepository;
        this.openFoodFactsApiService = openFoodFactsApiService;
    }

    public Mono<FoodProduct> getFoodItemByBarcode(String barcode){
        return this.openFoodFactsApiService.getFoodItemByBarcode(barcode);
    }

    public Mono<SearchItems> searchFoodItemsBySearchTerm(String terms){
        return this.openFoodFactsApiService.searchFoodItemsBySearchTerm(terms);
    }

    public void logFoodItem(LogFoodProductDTO logFoodProductDTO){
        Mono<LogFoodProduct> logFoodProduct = this.openFoodFactsApiService.getFoodItemByBarcode(logFoodProductDTO.getBarcode())
                .map(foodProduct -> {
                    var lfp = new LogFoodProduct();
                    lfp.setFoodProductBarcode(foodProduct.getBarcode());
                    lfp.setDate(logFoodProductDTO.getDate());
                    return lfp;
                });
        this.logFoodProductRepository.save(logFoodProduct.block());
    }

    public Flux<FoodProduct> getFoodItemsByDate(LocalDate date){
        List<LogFoodProduct> logFoodProducts = logFoodProductRepository.findAllByDate(date);
        return Flux.fromIterable(logFoodProducts)
                .flatMap(logFoodProduct -> openFoodFactsApiService.getFoodItemByBarcode(logFoodProduct.getFoodProductBarcode()));

    }
}
