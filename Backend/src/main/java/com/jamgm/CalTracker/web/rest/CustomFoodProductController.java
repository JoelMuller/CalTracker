package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.service.CustomFoodProductService;
import com.jamgm.CalTracker.web.rest.DTO.CustomFoodProductDTO;
import com.jamgm.CalTracker.web.rest.transformer.CustomFoodProductTransformer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/custom-food-item")
public class CustomFoodProductController {
    private final CustomFoodProductService customFoodProductService;
    public CustomFoodProductController(CustomFoodProductService customFoodProductService){
        this.customFoodProductService = customFoodProductService;
    }

    @PostMapping
    public ResponseEntity<CustomFoodProductDTO> createCustomFoodProduct(@RequestBody @Valid CustomFoodProductDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.customFoodProductService.createCustomFoodProduct(dto));
    }

    @GetMapping(value = "/{userId}/{customFoodProductId}")
    public ResponseEntity<CustomFoodProductDTO> getCustomFoodProduct(@PathVariable("userId") final long userId, @PathVariable("customFoodProductId") final long foodId){
        return ResponseEntity.ok(this.customFoodProductService.getCustomFoodProduct(userId, foodId));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<List<CustomFoodProductDTO>> getCustomFoodProductsByUser(@PathVariable("userId") final long userId){
        List<CustomFoodProductDTO> customFoodProductsDto = this.customFoodProductService.getAllCustomFoodProductsByUser(userId);
        return ResponseEntity.ok(customFoodProductsDto);
    }

    @PutMapping
    public ResponseEntity<CustomFoodProductDTO> updateCustomFoodProduct(@RequestBody @Valid CustomFoodProductDTO dto){
        return ResponseEntity.ok(this.customFoodProductService
                .updateCustomFoodProduct(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCustomFoodProduct(@PathVariable("customFoodProductId")final long id){
        this.customFoodProductService.deleteCustomFoodProduct(id);
        return ResponseEntity.noContent().build();
    }
}
