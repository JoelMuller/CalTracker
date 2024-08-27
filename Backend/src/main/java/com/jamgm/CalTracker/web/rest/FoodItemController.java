package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.service.FoodProductService;
import com.jamgm.CalTracker.web.rest.DTO.LogFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.ProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.SearchItemsDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
public class FoodItemController {
    private final FoodProductService foodProductService;

    public FoodItemController(FoodProductService foodProductService){
        this.foodProductService = foodProductService;
    }

    @GetMapping(value = "/product/{barcode}")
    @Operation(summary = "Get nutrition information by barcode")
    public Mono<ProductDTO> getFoodItemByBarcode(@PathVariable("barcode") final String barcode){
        Mono<FoodProduct> product = this.foodProductService.getFoodItemByBarcode(barcode);
        return product.map(FoodProductTransformer::toDto);
    }

    @GetMapping(value = "/search")
    public Mono<SearchItemsDTO> searchFoodItemsBySearchTerm(@RequestParam("search_terms") final String terms,
                                                            @RequestParam("page") final int page){
        Mono<SearchItems> products = this.foodProductService.searchFoodItemsBySearchTerm(terms, page);
        return products.map(SearchItemsTransformer::toDto);
    }

    @PostMapping(value = "/log", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Logs given food item barcode to the date")
    public void logFoodItem(@RequestBody @Valid LogFoodProductDTO logFoodProductDTO){
        this.foodProductService.logFoodItem(logFoodProductDTO);
    }

    @GetMapping(value = "/getItemsConsumedByDay/{date}")
    @Operation(summary = "Get all food items consumed by given date")
    public Flux<FoodProduct> getAllItemsConsumedByDate(@PathVariable("date") final String date){
        LocalDate givenDate = LocalDate.parse(date);
        return this.foodProductService.getFoodItemsByDate(givenDate);
    }
}
