package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.FoodItem;
import com.jamgm.CalTracker.web.rest.DTO.FoodItemDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodItemTransformer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenFoodFactsApiService {
    private final WebClient webClientSearch;
    private final WebClient webClientOther;

    public OpenFoodFactsApiService(WebClient.Builder webClientBuilder){
        this.webClientSearch = webClientBuilder.baseUrl("https://nl.openfoodfacts.org/cgi/search.pl").build();
        this.webClientOther = webClientBuilder.baseUrl("https://nl.openfoodfacts.org/api/v2").build();
    }

    public Mono<FoodItem> getFoodItemByBarcode(long barcode){
        return this.webClientOther.get()
                .uri("/product/" + barcode)
                .retrieve()
                .bodyToMono(FoodItemDTO.class)
                .map(FoodItemTransformer::fromDto);
    }

    /*public Mono<FoodItem[]> searchFoodItemsBySearchTerm(String searchTerm){
        return this.webClientSearch.get()
                .uri("/search_terms=" + searchTerm + "&json=1")
                .retrieve()
                .bodyToMono(); //make similar file to openfoodfactsdto but for search
    }*/
}
