package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.LogFoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.LogFoodProductRepository;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.FoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.LogFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.ProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.SearchItemsDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FoodProductService {
    private final LogFoodProductRepository logFoodProductRepository;
    private final UserRepository userRepository;
    //Searching and getting specific food items goes via openfoodfactsapi
    private final OpenFoodFactsApiService openFoodFactsApiService;

    public FoodProductService(OpenFoodFactsApiService openFoodFactsApiService,
                              LogFoodProductRepository logFoodProductRepository,
                              UserRepository userRepository){
        this.logFoodProductRepository = logFoodProductRepository;
        this.userRepository = userRepository;
        this.openFoodFactsApiService = openFoodFactsApiService;
    }

    public Mono<ProductDTO> getFoodItemByBarcode(String barcode){
        return this.openFoodFactsApiService.getFoodItemByBarcode(barcode)
                .map(FoodProductTransformer::toDto);
    }

    public Mono<SearchItemsDTO> searchFoodItemsBySearchTerm(String terms, int page){
        return this.openFoodFactsApiService.searchFoodItemsBySearchTerm(terms, page)
                .map(SearchItemsTransformer::toDto);
    }

    public Flux<ProductDTO> getFoodItemsByDate(LocalDate date, long userId){
        List<LogFoodProduct> logFoodProducts = logFoodProductRepository.findAllByDateAndUserId(date, userId);
        return Flux.fromIterable(logFoodProducts)
                .flatMap(logFoodProduct -> openFoodFactsApiService.getFoodItemByBarcode(logFoodProduct.getFoodProductBarcode()))
                .map(FoodProductTransformer::toDto);
    }

    public Mono<Double> getProteinConsumedByDay(Flux<ProductDTO> productFlux){
        return productFlux.map(product -> product.getNutriments().getProteins100g())
                .reduce(Double::sum);
    }

    public void logFoodItem(LogFoodProductDTO logFoodProductDTO){
        if(this.userRepository.existsById(logFoodProductDTO.getUserId())) {
            User user = this.userRepository.findById(logFoodProductDTO.getUserId()).get();
            if(logFoodProductDTO.getFoodProductBarcode() != null) {
                Mono<LogFoodProduct> logFoodProduct = this.openFoodFactsApiService.getFoodItemByBarcode(logFoodProductDTO.getFoodProductBarcode())
                        .map(foodProduct -> LogFoodProduct.builder()
                                .foodProductBarcode(foodProduct.getBarcode())
                                .date(logFoodProductDTO.getDate())
                                .user(user)
                                .build());
                this.logFoodProductRepository.save(logFoodProduct.block());
            }else{
                LogFoodProduct lfp = LogFoodProduct.builder()
                        .customFoodProduct(logFoodProductDTO.getCustomFoodProduct())
                        .date(logFoodProductDTO.getDate())
                        .user(user)
                        .build();
                this.logFoodProductRepository.save(lfp);
            }
        }else{
            throw new RuntimeException("Invalid user id");
        }
    }
}
