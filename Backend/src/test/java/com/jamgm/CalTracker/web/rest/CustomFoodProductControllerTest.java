package com.jamgm.CalTracker.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamgm.CalTracker.service.CustomFoodProductService;
import com.jamgm.CalTracker.web.rest.DTO.CustomFoodProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomFoodProductController.class)
public class CustomFoodProductControllerTest {

    @MockBean
    private CustomFoodProductService customFoodProductService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    private CustomFoodProductDTO customFoodProductDTO;
    private CustomFoodProductDTO customFoodProductDTOWithoutId;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.customFoodProductDTO = CustomFoodProductDTO.builder()
                ._id("1")
                .product_name("Custom Product")
                .serving_size("100g")
                .build();
        this.customFoodProductDTOWithoutId = CustomFoodProductDTO.builder()
                .product_name("Custom Product")
                .serving_size("100g")
                .build();
    }

    @Test
    public void testCreateCustomFoodProduct() throws Exception {
        when(customFoodProductService.createCustomFoodProduct(any(CustomFoodProductDTO.class)))
                .thenReturn(customFoodProductDTO);

        this.mockMvc.perform(post("/custom-food-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customFoodProductDTOWithoutId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product_name").value("Custom Product"))
                .andExpect(jsonPath("$.serving_size").value("100g"));
    }

    @Test
    public void testGetCustomFoodProduct() throws Exception {
        when(customFoodProductService.getCustomFoodProduct(1L, 1L))
                .thenReturn(customFoodProductDTO);

        this.mockMvc.perform(get("/custom-food-item/{userId}/{customFoodProductId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Custom Product"))
                .andExpect(jsonPath("$.serving_size").value("100g"));
    }

    @Test
    public void testGetCustomFoodProductsByUser() throws Exception {
        List<CustomFoodProductDTO> customFoodProducts = Arrays.asList(customFoodProductDTO);
        when(customFoodProductService.getAllCustomFoodProductsByUser(1L))
                .thenReturn(customFoodProducts);

        this.mockMvc.perform(get("/custom-food-item/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product_name").value("Custom Product"))
                .andExpect(jsonPath("$[0].serving_size").value("100g"));
    }

    @Test
    public void testUpdateCustomFoodProduct() throws Exception {
        when(customFoodProductService.updateCustomFoodProduct(any(CustomFoodProductDTO.class)))
                .thenReturn(customFoodProductDTO);

        this.mockMvc.perform(put("/custom-food-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customFoodProductDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Custom Product"))
                .andExpect(jsonPath("$.serving_size").value("100g"));
    }

    @Test
    public void testDeleteCustomFoodProduct() throws Exception {
        doNothing().when(customFoodProductService).deleteCustomFoodProduct(1L);

        this.mockMvc.perform(delete("/custom-food-item/{customFoodProductId}", 1L))
                .andExpect(status().isNoContent());
    }
}
