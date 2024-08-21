package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.service.FoodProductService;
import com.jamgm.CalTracker.web.rest.DTO.ProductDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FoodItemController {
    private final FoodProductService foodProductService;

    public FoodItemController(FoodProductService foodProductService){
        this.foodProductService = foodProductService;
    }

    @GetMapping(value = "/{barcode}")
    @Operation(summary = "Get nutrition information by barcode")
    public Mono<ProductDTO> getFoodItemByBarcode(@PathVariable("barcode") final long barcode){
        var product = this.foodProductService.getFoodItemByBarcode(barcode);
        return product.map(FoodProductTransformer::toDto);
//        return this.foodProductService.getFoodItemByBarcode(barcode);
    }
}
