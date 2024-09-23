package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.service.FoodProductService;
import com.jamgm.CalTracker.web.rest.DTO.*;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/food-item")
public class FoodProductController {
    private final FoodProductService foodProductService;

    public FoodProductController(FoodProductService foodProductService){
        this.foodProductService = foodProductService;
    }

    @GetMapping(value = "/product/{barcode}")
    @Operation(summary = "Get nutrition information by barcode")
    public ResponseEntity<Mono<ProductDTO>> getFoodItemByBarcode(@PathVariable("barcode") final String barcode){
        Mono<ProductDTO> product = this.foodProductService.getFoodItemByBarcode(barcode);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Mono<SearchItemsDTO>> searchFoodItemsBySearchTerm(@RequestParam("search_terms") final String terms,
                                                            @RequestParam("page") final int page){
        Mono<SearchItemsDTO> products = this.foodProductService.searchFoodItemsBySearchTerm(terms, page);
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/log")
    @Operation(summary = "Logs given custom food item or barcode for a user")
    public ResponseEntity<Void> logFoodItem(@RequestBody @Valid LogFoodProductDTO logFoodProductDTO){

        this.foodProductService.logFoodItem(logFoodProductDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/get-items-logged-by-day")
    @Operation(summary = "Get all food items consumed by given date")
    public ResponseEntity<List<LoggedFoodProductDTO>> getAllLoggedFoodItemsConsumedByDate(@RequestParam("userId") final long userId,
                                                                                @RequestParam("date") final String date){
        LocalDate givenDate = LocalDate.parse(date);
        return ResponseEntity.ok(this.foodProductService.getAllLoggedFoodItemsByDate(givenDate, userId));
    }
    @GetMapping(value = "/get-protein-consumed-by-day")
    public ResponseEntity<Double> getProteinsConsumedByDay(@RequestParam("userId") final long userId,
                                                 @RequestParam("date") final String date){
        LocalDate givenDate = LocalDate.parse(date);
        return ResponseEntity.ok(this.foodProductService
                .getProteinConsumedByDay(givenDate, userId));
    }

    @GetMapping(value = "/get-calories-consumed-by-day")
    public ResponseEntity<Double> getCaloriesConsumedByDay(@RequestParam("userId") final long userId,
                                                                 @RequestParam("date") final String date){
        LocalDate givenDate = LocalDate.parse(date);
        return ResponseEntity.ok(this.foodProductService
                .getCaloriesConsumedByDay(givenDate, userId));
    }
}
