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
    public ResponseEntity<ProductDTO> getFoodItemByBarcode(@PathVariable("barcode") final String barcode) {
        ProductDTO product = this.foodProductService.getFoodItemByBarcode(barcode);

        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<SearchItemsDTO> searchFoodItemsBySearchTerm(
            @RequestParam("search_terms") final String terms,
            @RequestParam("page") final int page) {

        SearchItemsDTO products = this.foodProductService.searchFoodItemsBySearchTerm(terms, page);

        if (products != null) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping(value = "/log")
    @Operation(summary = "Logs given custom food item or barcode for a user")
    public ResponseEntity<Void> logFoodItem(@RequestBody @Valid LogFoodProductDTO logFoodProductDTO){

        this.foodProductService.logFoodItem(logFoodProductDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/log/{id}")
    @Operation(summary = "Deletes the log of given id")
    public ResponseEntity<Void> deleteLoggedFoodItem(@PathVariable("id") final long logId){
        this.foodProductService.deleteLoggedFoodItem(logId);
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
