package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.Interceptors.RateLimitFilter;
import com.jamgm.CalTracker.authentication.JwtRequestFilter;
import com.jamgm.CalTracker.service.FoodProductService;
import com.jamgm.CalTracker.web.rest.DTO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodProductController.class)
public class FoodProductControllerTest {
    @MockBean
    private FoodProductService foodProductService;
    @MockBean
    private RateLimitFilter rateLimitFilter;
    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    private ProductDTO productDTO;
    private SearchItemsDTO searchItemsDTO;
    private List<LoggedFoodProductDTO> loggedFoodProductDTOList;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        productDTO = ProductDTO.builder()
                ._id("1")
                .product_name("test")
                .serving_size("100g")
                .categories("test")
                .build();

        searchItemsDTO = SearchItemsDTO.builder()
                .total(2)
                .currentPage(1)
                .foodProducts(List.of(ProductDTO.builder()
                                ._id("1")
                                .product_name("test1")
                                .categories("testing")
                                .serving_size("100g")
                                .nutriments(NutrimentsDTO.builder()
                                        .energyKcal100g(100.0)
                                        .proteins100g(50.0)
                                        .build())
                                .build(),
                        ProductDTO.builder()
                                ._id("2")
                                .product_name("test2")
                                .categories("testing")
                                .serving_size("50g")
                                .nutriments(NutrimentsDTO.builder()
                                        .energyKcal100g(70.0)
                                        .proteins100g(20.0)
                                        .build())
                                .build()))
                .build();

        loggedFoodProductDTOList = List.of(
                LoggedFoodProductDTO.builder()
                        .id(1L)
                        .gramsConsumed(50.0)
                        .product_name("testing1")
                        .serving_size("100g")
                        .categories("testing")
                        .nutriments(NutrimentsDTO.builder()
                                .energyKcal100g(100.0)
                                .proteins100g(50.0)
                                .build())
                        .build(),
                LoggedFoodProductDTO.builder()
                        .id(2L)
                        .gramsConsumed(20.0)
                        .product_name("testing2")
                        .serving_size("50g")
                        .categories("testing")
                        .nutriments(NutrimentsDTO.builder()
                                .energyKcal100g(100.0)
                                .proteins100g(50.0)
                                .build())
                        .build()
        );
    }

    @Test
    public void testGetFoodItemByBarcode() throws Exception {
        when(foodProductService.getFoodItemByBarcode("0000")).thenReturn(productDTO);
        this.mockMvc.perform(get("/food-item/product/{barcode}", "0000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("test"));
    }

    @Test
    public void testSearchFoodItemsBySearchTerm() throws Exception {
        String searchTerms = "test";
        when(foodProductService.searchFoodItemsBySearchTerm(anyString(), anyInt())).thenReturn(searchItemsDTO);
        this.mockMvc.perform(get("/food-item/search")
                        .param("search_terms", searchTerms)
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value("2"))
                .andExpect(jsonPath("$.products[0].product_name").value("test1"));
    }

    @Test
    public void testLogFoodItemBarcode() throws Exception {
        doNothing().when(foodProductService).logFoodItem(any(LogFoodProductDTO.class));
        String requestBody = """
                    {
                        "gramsConsumed": 100,
                        "foodProductBarcode": "0000",
                        "date": "2024-10-23",
                        "userId": 1
                    }
                """;
        this.mockMvc.perform(post("/food-item/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogFoodItemCustomFoodProduct() throws Exception {
        doNothing().when(foodProductService).logFoodItem(any(LogFoodProductDTO.class));
        String requestBody = """
                    {
                        "date": "2024-10-23",
                        "userId": 1,
                        "gramsConsumed": 100,
                        "customFoodProduct": {
                            "id": "1",
                            "product_name": "test",
                            "categories": "testing",
                            "serving_size": "100",
                            "nutriments": {
                                "energy-kcal_100g": 0,
                                "proteins_100g": 0,
                                "carbohydrates_100g": 0,
                                "sugars_100g": 0,
                                "fat_100g": 0,
                                "saturated-fat_100g": 0,
                                "fiber_100g": 0,
                                "sodium_100g": 0
                            }
                        }
                    }
                """;
        this.mockMvc.perform(post("/food-item/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogFoodItemWithoutDate() throws Exception {
        doNothing().when(foodProductService).logFoodItem(any(LogFoodProductDTO.class));
        String requestBody = """
                    {
                        "gramsConsumed": 100,
                        "foodProductBarcode": "0000",
                        "userId": 1
                    }
                """;
        this.mockMvc.perform(post("/food-item/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteLoggedFoodItem() throws Exception {
        doNothing().when(foodProductService).deleteLoggedFoodItem(anyLong());

        this.mockMvc.perform(delete("/food-item/log/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllLoggedFoodItemsConsumedByDate() throws Exception {
        when(foodProductService.getAllLoggedFoodItemsByDate(any(LocalDate.class), anyLong())).thenReturn(loggedFoodProductDTOList);
        this.mockMvc.perform(get("/food-item/get-items-logged-by-day")
                        .param("date", "2024-01-01")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product_name").value("testing1"));
    }

    @Test
    public void testGetProteinConsumedByDay() throws Exception {
        when(foodProductService.getProteinConsumedByDay(any(LocalDate.class), anyLong())).thenReturn(50.0);
        this.mockMvc.perform(get("/food-item/get-protein-consumed-by-day")
                .param("userId", "1")
                .param("date", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("50.0"));
    }

    @Test
    public void testGetCaloriesConsumedByDay() throws Exception {
        when(foodProductService.getCaloriesConsumedByDay(any(LocalDate.class), anyLong())).thenReturn(50.0);
        this.mockMvc.perform(get("/food-item/get-calories-consumed-by-day")
                        .param("userId", "1")
                        .param("date", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().string("50.0"));
    }
}
