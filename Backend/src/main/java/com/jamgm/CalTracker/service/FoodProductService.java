package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.LogFoodProduct;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.LogFoodProductRepository;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.*;
import com.jamgm.CalTracker.web.rest.transformer.CustomFoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.LoggedFoodProductsTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public ProductDTO getFoodItemByBarcode(String barcode) {
        FoodProduct foodProduct = this.openFoodFactsApiService.getFoodItemByBarcode(barcode);
        return FoodProductTransformer.toDto(foodProduct);
    }

    public SearchItemsDTO searchFoodItemsBySearchTerm(String terms, int page) {
        return SearchItemsTransformer.toDto(this.openFoodFactsApiService.searchFoodItemsBySearchTerm(terms, page));
    }

    public List<LoggedFoodProductDTO> getAllLoggedFoodItemsByDate(LocalDate date, long userId) {
        List<LogFoodProduct> logFoodProducts = logFoodProductRepository.findAllByDateAndUserId(date, userId);
        List<LoggedFoodProductDTO> allProducts = new ArrayList<>();

        for (LogFoodProduct logFoodProduct : logFoodProducts) {
            if (logFoodProduct.getFoodProductBarcode() != null) {
                FoodProduct foodProduct = openFoodFactsApiService.getFoodItemByBarcode(logFoodProduct.getFoodProductBarcode());
                allProducts.add(LoggedFoodProductsTransformer.toDto(foodProduct, logFoodProduct.getId(),
                        logFoodProduct.getGramsConsumed()));
            } else {
                CustomFoodProduct customFoodProduct = logFoodProduct.getCustomFoodProduct();
                allProducts.add(LoggedFoodProductsTransformer.toDto(customFoodProduct, logFoodProduct.getId(),
                        logFoodProduct.getGramsConsumed()));
            }
        }
        return allProducts;
    }

    public Double getProteinConsumedByDay(LocalDate date, long userId){
        return getAllLoggedFoodItemsByDate(date, userId)
                .stream()
                .map(product -> {
                    double proteins = product.getNutriments().getProteins100g();
                    return product.getGramsConsumed() / 100 * proteins;
                })
                .reduce((double) 0, Double::sum);
    }

    public Double getCaloriesConsumedByDay(LocalDate date, long userId){
        return getAllLoggedFoodItemsByDate(date, userId)
                .stream()
                .map(logFoodProduct -> {
                    double kcal = logFoodProduct.getNutriments().getEnergyKcal100g();
                    return logFoodProduct.getGramsConsumed() / 100 * kcal;
                })
                .reduce((double) 0, Double::sum);
    }

    public void logFoodItem(LogFoodProductDTO logFoodProductDTO) {
        if (this.userRepository.existsById(logFoodProductDTO.getUserId())) {
            User user = this.userRepository.findById(logFoodProductDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (logFoodProductDTO.getFoodProductBarcode() != null) {
                FoodProduct foodProduct = this.openFoodFactsApiService.getFoodItemByBarcode(logFoodProductDTO.getFoodProductBarcode());
                LogFoodProduct logFoodProduct = LogFoodProduct.builder()
                        .gramsConsumed(logFoodProductDTO.getGramsConsumed())
                        .foodProductBarcode(foodProduct.getBarcode())
                        .productName(foodProduct.getProduct_name())
                        .date(logFoodProductDTO.getDate())
                        .user(user)
                        .build();

                this.logFoodProductRepository.save(logFoodProduct);
            } else {
                LogFoodProduct lfp = LogFoodProduct.builder()
                        .gramsConsumed(logFoodProductDTO.getGramsConsumed())
                        .customFoodProduct(CustomFoodProductTransformer.fromDto(logFoodProductDTO.getCustomFoodProduct(), user))
                        .productName(logFoodProductDTO.getCustomFoodProduct().getProduct_name())
                        .date(logFoodProductDTO.getDate())
                        .user(user)
                        .build();

                this.logFoodProductRepository.save(lfp);
            }
        } else {
            throw new RuntimeException("Invalid user id");
        }
    }

    public void deleteLoggedFoodItem(long logId){
        this.logFoodProductRepository.deleteById(logId);
    }
}
