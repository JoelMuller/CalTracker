package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.*;
import com.jamgm.CalTracker.repository.LogFoodProductRepository;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.CustomFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.LogFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.LoggedFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.NutrimentsDTO;
import com.jamgm.CalTracker.web.rest.transformer.CustomFoodProductTransformer;
import com.jamgm.CalTracker.web.rest.transformer.LoggedFoodProductsTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FoodProductServiceTest {
    @Mock
    private LogFoodProductRepository logFoodProductRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OpenFoodFactsApiService openFoodFactsApiService;
    @InjectMocks
    private FoodProductService foodProductService;
    private List<LogFoodProduct> loggedFoodProducts = new ArrayList<>();
    private List<LoggedFoodProductDTO> loggedFoodProductDTOs = new ArrayList<>();
    private FoodProduct foodProduct;
    private User user;

    @BeforeEach
    public void beforeEach() {
        //fill loggedfoodproducts with one of barcode and one of customfoodproduct
        //also fill in nutriments for the protein consumedbyday and caloriesconsumedbyday methods
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .name("test")
                .password("testing")
                .weight(80.0).build();

        foodProduct = FoodProduct.builder()
                .product_name("barcode")
                .barcode("0000")
                .serving_size("100g")
                .categories(List.of("testing"))
                .nutriments(Nutriments.builder()
                        .proteins100g(50.0)
                        .carbohydrates100g(50.0)
                        .energyKcal100g(20.0)
                        .build())
                .build();

        LogFoodProduct logFoodProduct = LogFoodProduct.builder()
                .id(1)
                .productName("barcode")
                .gramsConsumed(50.0)
                .foodProductBarcode("0000")
                .date(LocalDate.of(2024, 6, 10))
                .build();

        loggedFoodProducts.add(logFoodProduct);
        loggedFoodProductDTOs.add(LoggedFoodProductsTransformer.toDto(foodProduct, logFoodProduct.getId(), logFoodProduct.getGramsConsumed()));

        CustomFoodProduct customFoodProduct = CustomFoodProduct.builder()
                .id(1)
                .product_name("custom")
                .serving_size("200g")
                .user(user)
                .nutriments(Nutriments.builder()
                        .proteins100g(50.0)
                        .carbohydrates100g(50.0)
                        .energyKcal100g(20.0)
                        .build())
                .build();

        LogFoodProduct logFoodProduct1 = LogFoodProduct.builder()
                .id(2)
                .customFoodProduct(customFoodProduct)
                .gramsConsumed(100.0)
                .date(LocalDate.of(2024, 6, 10))
                .user(user)
                .build();

        loggedFoodProducts.add(logFoodProduct1);
        loggedFoodProductDTOs.add(LoggedFoodProductsTransformer.toDto(customFoodProduct, logFoodProduct1.getId(), logFoodProduct1.getGramsConsumed()));
        user.setLoggedFoodProducts(loggedFoodProducts);
    }

    @Test
    public void testGetAllLoggedFoodItemsByDate() {
        try (MockedStatic<LoggedFoodProductsTransformer> mockedTransformer = mockStatic(LoggedFoodProductsTransformer.class)) {
            //when call to logfoodproductrepository.findallbydateanduserid
            when(logFoodProductRepository.findAllByDateAndUserId(any(LocalDate.class), anyLong())).thenReturn(loggedFoodProducts);
            //when call to openfoodfactsapiservice.getfooditembybarcode
            when(openFoodFactsApiService.getFoodItemByBarcode(anyString())).thenReturn(foodProduct);

            //mock transformer methods, be aware of the different methods for custom and barcode food products
            mockedTransformer.when(() -> LoggedFoodProductsTransformer.toDto(any(FoodProduct.class), anyLong(), anyDouble())).thenReturn(loggedFoodProductDTOs.get(0));
            mockedTransformer.when(() -> LoggedFoodProductsTransformer.toDto(any(CustomFoodProduct.class), anyLong(), anyDouble())).thenReturn(loggedFoodProductDTOs.get(1));

            //act the method under test
            List<LoggedFoodProductDTO> result = foodProductService.getAllLoggedFoodItemsByDate(LocalDate.of(2024, 6, 10), 1L);

            mockedTransformer.verify(() -> LoggedFoodProductsTransformer.toDto(any(FoodProduct.class), anyLong(), anyDouble()));
            mockedTransformer.verify(() -> LoggedFoodProductsTransformer.toDto(any(CustomFoodProduct.class), anyLong(), anyDouble()));

            //assert same result is same list as loggedfoodproductDTO
            assertEquals(loggedFoodProductDTOs, result);
        }
    }

    @Test
    public void testGetProteinConsumedByDay() {
        try (MockedStatic<LoggedFoodProductsTransformer> mockedTransformer = mockStatic(LoggedFoodProductsTransformer.class)) {
            when(logFoodProductRepository.findAllByDateAndUserId(any(LocalDate.class), anyLong())).thenReturn(loggedFoodProducts);
            when(openFoodFactsApiService.getFoodItemByBarcode(anyString())).thenReturn(foodProduct);

            mockedTransformer.when(() -> LoggedFoodProductsTransformer.toDto(any(FoodProduct.class), anyLong(), anyDouble())).thenReturn(loggedFoodProductDTOs.get(0));
            mockedTransformer.when(() -> LoggedFoodProductsTransformer.toDto(any(CustomFoodProduct.class), anyLong(), anyDouble())).thenReturn(loggedFoodProductDTOs.get(1));

            Double localResult = loggedFoodProductDTOs.stream()
                    .map(product -> {
                        double proteins = product.getNutriments().getProteins100g();
                        return product.getGramsConsumed() / 100 * proteins;
                    })
                    .reduce((double) 0, Double::sum);

            Double result = foodProductService.getProteinConsumedByDay(LocalDate.of(2024, 6, 10), 1L);

            assertEquals(localResult, result);
        }
    }

    @Test
    public void testGetCaloriesConsumedByDay() {
        try (MockedStatic<LoggedFoodProductsTransformer> mockedTransformer = mockStatic(LoggedFoodProductsTransformer.class)) {
            when(logFoodProductRepository.findAllByDateAndUserId(any(LocalDate.class), anyLong())).thenReturn(loggedFoodProducts);
            when(openFoodFactsApiService.getFoodItemByBarcode(anyString())).thenReturn(foodProduct);

            mockedTransformer.when(() -> LoggedFoodProductsTransformer.toDto(any(FoodProduct.class), anyLong(), anyDouble())).thenReturn(loggedFoodProductDTOs.get(0));
            mockedTransformer.when(() -> LoggedFoodProductsTransformer.toDto(any(CustomFoodProduct.class), anyLong(), anyDouble())).thenReturn(loggedFoodProductDTOs.get(1));

            Double localResult = loggedFoodProductDTOs.stream()
                    .map(logFoodProduct -> {
                        double kcal = logFoodProduct.getNutriments().getEnergyKcal100g();
                        return logFoodProduct.getGramsConsumed() / 100 * kcal;
                    })
                    .reduce((double) 0, Double::sum);

            Double result = foodProductService.getCaloriesConsumedByDay(LocalDate.of(2024, 6, 10), 1L);

            assertEquals(localResult, result);
        }
    }

    @Test
    public void testLogFoodItemBarcode() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(openFoodFactsApiService.getFoodItemByBarcode(anyString())).thenReturn(foodProduct);

        LogFoodProductDTO toLogFoodProduct = LogFoodProductDTO.builder()
                .id(3)
                .foodProductBarcode("1234")
                .gramsConsumed(10.0)
                .date(LocalDate.of(2024, 6, 10))
                .userId(user.getId())
                .build();
        foodProductService.logFoodItem(toLogFoodProduct);

        verify(logFoodProductRepository).save(any(LogFoodProduct.class));
    }

    @Test
    public void testLogFoodItemCustomFoodProduct() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        LogFoodProductDTO toLogFoodProduct = LogFoodProductDTO.builder()
                .id(3)
                .customFoodProduct(CustomFoodProductDTO.builder()
                        .id("2")
                        .product_name("custom")
                        .serving_size("100g")
                        .userId(user.getId())
                        .nutriments(NutrimentsDTO.builder()
                                .energyKcal100g(50.0)
                                .build())
                        .build())
                .gramsConsumed(10.0)
                .date(LocalDate.of(2024, 6, 10))
                .userId(user.getId())
                .build();
        foodProductService.logFoodItem(toLogFoodProduct);

        verify(logFoodProductRepository).save(any(LogFoodProduct.class));
    }

    @Test
    public void testLogFoodItemException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        LogFoodProductDTO toLogFoodProduct = LogFoodProductDTO.builder()
                .id(3)
                .foodProductBarcode("1234")
                .gramsConsumed(10.0)
                .date(LocalDate.of(2024, 6, 10))
                .userId(user.getId())
                .build();
        Exception thrown = assertThrows(RuntimeException.class,
                () -> foodProductService.logFoodItem(toLogFoodProduct));
        assertTrue(thrown.getMessage().contains("Invalid user id"));
    }

    @Test
    public void testDeleteLoggedFoodItem(){
        foodProductService.deleteLoggedFoodItem(1L);

        verify(logFoodProductRepository, times(1)).deleteById(anyLong());
    }
}
