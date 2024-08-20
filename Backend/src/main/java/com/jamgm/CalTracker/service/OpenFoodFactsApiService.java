package com.jamgm.CalTracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenFoodFactsApiService {
    private final WebClient webClientSearch;
    private final WebClient webClientOther;

    public OpenFoodFactsApiService(WebClient.Builder webClientBuilder){
        this.webClientSearch = webClientBuilder.baseUrl("https://nl.openfoodfacts.org/cgi/search.pl").build();
        this.webClientOther = webClientBuilder.baseUrl("https://nl.openfoodfacts.org/api/v2").build();
    }

    //some functions for calling the openfoodfactsapi's

}
