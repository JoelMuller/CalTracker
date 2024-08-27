package com.jamgm.CalTracker.service;

//import com.jamgm.CalTracker.repository.FoodItemRepository;
import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.LogFoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.repository.LogFoodProductRepository;
import com.jamgm.CalTracker.repository.UserRepository;
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
    private final UserRepository userRepository;
    //Searching and getting specific food items goes via openfoodfactsapi
    private final OpenFoodFactsApiService openFoodFactsApiService;

    public FoodProductService(OpenFoodFactsApiService openFoodFactsApiService,
                              LogFoodProductRepository logFoodProductRepository,
                              UserRepository userRepository){
        //this.foodItemRepository = foodItemRepository;
        this.logFoodProductRepository = logFoodProductRepository;
        this.userRepository = userRepository;
        this.openFoodFactsApiService = openFoodFactsApiService;
    }

    public Mono<FoodProduct> getFoodItemByBarcode(String barcode){
        return this.openFoodFactsApiService.getFoodItemByBarcode(barcode);
    }

    public Mono<SearchItems> searchFoodItemsBySearchTerm(String terms, int page){
        return this.openFoodFactsApiService.searchFoodItemsBySearchTerm(terms, page);
    }

    public void logFoodItem(LogFoodProductDTO logFoodProductDTO){
        if(this.userRepository.existsById(logFoodProductDTO.getUserId())) {
            Mono<LogFoodProduct> logFoodProduct = this.openFoodFactsApiService.getFoodItemByBarcode(logFoodProductDTO.getBarcode())
                    .map(foodProduct -> {
                        var lfp = new LogFoodProduct();
                        lfp.setFoodProductBarcode(foodProduct.getBarcode());
                        lfp.setDate(logFoodProductDTO.getDate());
                        lfp.setUser(this.userRepository.findById(logFoodProductDTO.getUserId()).get());
                        return lfp;
                    });
            this.logFoodProductRepository.save(logFoodProduct.block());
        }else{
            throw new RuntimeException("Invalid user id");
        }
    }

    public Flux<FoodProduct> getFoodItemsByDate(LocalDate date, long userId){
        List<LogFoodProduct> logFoodProducts = logFoodProductRepository.findAllByDateAndUserId(date, userId);
        return Flux.fromIterable(logFoodProducts)
                .flatMap(logFoodProduct -> openFoodFactsApiService.getFoodItemByBarcode(logFoodProduct.getFoodProductBarcode()));
    }

    public Mono<Double> getProteinConsumedByDay(Flux<FoodProduct> productFlux){
        return productFlux.map(product -> product.getNutriments().getProteins100g())
                .reduce(Double::sum);
    }
}
