package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.FoodProduct;
import com.jamgm.CalTracker.model.SearchItems;
import com.jamgm.CalTracker.web.rest.DTO.FoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.ProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.SearchItemsDTO;
import com.jamgm.CalTracker.web.rest.transformer.FoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.SearchItemsTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OpenFoodFactsApiServiceTest {
    @Mock
    private RestTemplateBuilder restTemplateBuilder;
    @Mock
    private RestTemplate restTemplateSearch;
    @Mock
    private RestTemplate restTemplateOther;
    @Autowired
    private OpenFoodFactsApiService openFoodFactsApiService;
    private FoodProductDTO foodProductDTO;
    private FoodProduct foodProduct;
    private SearchItemsDTO searchItemsDTO;
    private SearchItems searchItems;

    @BeforeEach
    public void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Mock the behavior of RestTemplateBuilder
        when(restTemplateBuilder.rootUri("https://nl.openfoodfacts.org/cgi/search.pl")).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplateSearch);

        when(restTemplateBuilder.rootUri("https://nl.openfoodfacts.org/api/v2")).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplateOther);

        // Initialize data
        foodProductDTO = FoodProductDTO.builder()
                .product(
                        ProductDTO.builder()
                                ._id("0000")
                                .product_name("test")
                                .serving_size("100g")
                                .categories("food")
                                .build()
                )
                .code("0000")
                .build();

        foodProduct = FoodProduct.builder()
                .product_name("test")
                .barcode("0000")
                .serving_size("100g")
                .categories(List.of("food"))
                .build();

        searchItemsDTO = SearchItemsDTO.builder()
                .currentPage(1)
                .total(1)
                .foodProducts(List.of(ProductDTO.builder()
                        ._id("1")
                        .product_name("test")
                        .serving_size("100g")
                        .build()))
                .build();

        searchItems = SearchItems.builder()
                .currentPage(1)
                .total(1)
                .foodProducts(List.of(FoodProduct.builder()
                        .product_name("test")
                        .serving_size("100g")
                        .barcode("1")
                        .build()))
                .build();
    }

    @Test
    void testGetFoodItemByBarcode() {
        when(restTemplateOther.getForEntity(Mockito.anyString(), Mockito.eq(FoodProductDTO.class)))
                .thenReturn(new ResponseEntity<>(foodProductDTO, HttpStatus.OK));

        try (MockedStatic<FoodProductTransformer> mockedTransformer = mockStatic(FoodProductTransformer.class)) {
            mockedTransformer.when(() -> FoodProductTransformer.fromDto(any(FoodProductDTO.class))).thenReturn(foodProduct);

            FoodProduct result = openFoodFactsApiService.getFoodItemByBarcode(anyString());
            Assertions.assertEquals(foodProduct, result);
        }
    }

    @Test
    void testSearchFoodItemsBySearchTerm() {
        when(restTemplateSearch.getForEntity(Mockito.anyString(), Mockito.eq(SearchItemsDTO.class)))
                .thenReturn(new ResponseEntity<>(searchItemsDTO, HttpStatus.OK));

        try (MockedStatic<SearchItemsTransformer> mockedTransformer = mockStatic(SearchItemsTransformer.class)) {
            mockedTransformer.when(() -> SearchItemsTransformer.fromDto(any(SearchItemsDTO.class))).thenReturn(searchItems);

            SearchItems result = openFoodFactsApiService.searchFoodItemsBySearchTerm("", 1);
            Assertions.assertEquals(searchItems, result);
        }
    }
}
