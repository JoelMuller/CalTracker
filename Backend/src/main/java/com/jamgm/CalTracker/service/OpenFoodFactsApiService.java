package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.web.rest.DTO.FoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.SearchItemsDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OpenFoodFactsApiService {

    private final RestTemplate restTemplateSearch;
    private final RestTemplate restTemplateOther;

    public OpenFoodFactsApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateSearch = restTemplateBuilder
                .rootUri("https://nl.openfoodfacts.org/cgi/search.pl")
                .build();

        this.restTemplateOther = restTemplateBuilder
                .rootUri("https://nl.openfoodfacts.org/api/v2")
                .build();
    }

    public FoodProduct getFoodItemByBarcode(String barcode) {
        try {
            String url = "/product/" + barcode;
            ResponseEntity<FoodProductDTO> responseEntity = this.restTemplateOther
                    .getForEntity(url, FoodProductDTO.class);

            FoodProductDTO foodProductDTO = responseEntity.getBody();
            if (foodProductDTO != null) {
                return FoodProductTransformer.fromDto(foodProductDTO);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while fetching food item by barcode: " + e.getMessage());
            return null;
        }
        return null;
    }

    public SearchItems searchFoodItemsBySearchTerm(String searchTerm, int page) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString("https://nl.openfoodfacts.org/cgi/search.pl")
                    .queryParam("search_terms", searchTerm)
                    .queryParam("json", "1")
                    .queryParam("page_size", "10")
                    .queryParam("page", page)
                    .toUriString();

            ResponseEntity<SearchItemsDTO> responseEntity = this.restTemplateSearch
                    .getForEntity(url, SearchItemsDTO.class);

            SearchItemsDTO searchItemsDTO = responseEntity.getBody();
            if (searchItemsDTO != null) {
                return SearchItemsTransformer.fromDto(searchItemsDTO);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while searching for food items: " + e.getMessage());
        }
        return null;
    }
}
