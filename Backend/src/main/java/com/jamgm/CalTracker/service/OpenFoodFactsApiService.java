package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.web.rest.DTO.FoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.SearchItemsDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenFoodFactsApiService {
    private final WebClient webClientSearch;
    private final WebClient webClientOther;

    public OpenFoodFactsApiService(WebClient.Builder webClientBuilder){
        this.webClientSearch = webClientBuilder.baseUrl("https://nl.openfoodfacts.org/cgi/search.pl")
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(500 * 1024)) //otherwise certain search requests won't load due to all the information received
                .build();
        this.webClientOther = webClientBuilder.baseUrl("https://nl.openfoodfacts.org/api/v2").build();
    }

    public Mono<FoodProduct> getFoodItemByBarcode(String barcode){
        return this.webClientOther.get()
                .uri("/product/" + barcode)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(FoodProductDTO.class)
                .map(FoodProductTransformer::fromDto);
    }

    public Mono<SearchItems> searchFoodItemsBySearchTerm(String searchTerm, int page){
        //api v2 has more customization to receive items with only the info you need
        //with api v2 you can't give a search term to the call and with v1 you can but
        //you get a bunch of data that is not needed
        return this.webClientSearch.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("search_terms", searchTerm)
                        .queryParam("json", "1")
                        .queryParam("page_size", "10")
                        .queryParam("page", page)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SearchItemsDTO.class)
                .map(SearchItemsTransformer::fromDto);
    }
}
