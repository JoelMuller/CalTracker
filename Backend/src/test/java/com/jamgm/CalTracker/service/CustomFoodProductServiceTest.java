package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.CustomFoodProductRepository;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.CustomFoodProductDTO;
import com.jamgm.CalTracker.web.rest.DTO.NutrimentsDTO;
import com.jamgm.CalTracker.web.rest.transformer.CustomFoodProductTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomFoodProductServiceTest {
    @Mock
    private CustomFoodProductRepository customFoodProductRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomFoodProductService customFoodProductService;

    private User user;
    private CustomFoodProduct customFoodProduct;
    private CustomFoodProductDTO customFoodProductDTO;
    private CustomFoodProductDTO customFoodProductDTOWithoutUser;
    private NutrimentsDTO nutrimentsDTO;
    private Nutriments nutriments;

    @BeforeEach
    public void beforeEach() {
        reset(customFoodProductRepository);

        user = User.builder()
                .id(1)
                .name("testuser1")
                .email("test1@test.com")
                .password("testPassword")
                .weightLossPerWeek(0.50)
                .build();
        customFoodProduct = CustomFoodProduct.builder()
                .id(1L)
                .product_name("Test Product")
                .serving_size("100g")
                .user(user)
                .build();
        nutriments = Nutriments.builder()
                .id(1L)
                .energyKcal100g(100.0)
                .proteins100g(10.0)
                .carbohydrates100g(20.0)
                .sugars100g(5.0)
                .fat100g(10.0)
                .saturatedFat100g(2.0)
                .fiber100g(3.0)
                .sodium100g(1.0)
                .customFoodProduct(customFoodProduct)
                .build();
        customFoodProduct.setNutriments(nutriments);

        // Initialize CustomFoodProductDTO
        customFoodProductDTO = CustomFoodProductDTO.builder()
                .id("1")
                .product_name("Test Product")
                .serving_size("100g")
                .userId(1L)
                .build();
        nutrimentsDTO = NutrimentsDTO.builder()
                .energyKcal100g(100.0)
                .proteins100g(10.0)
                .carbohydrates100g(20.0)
                .sugars100g(5.0)
                .fat100g(10.0)
                .saturatedFat100g(2.0)
                .fiber100g(3.0)
                .sodium100g(1.0)
                .build();
        customFoodProductDTO.setNutriments(nutrimentsDTO);

        customFoodProductDTOWithoutUser = CustomFoodProductDTO.builder()
                .id("1")
                .product_name("Test Product")
                .serving_size("100g")
                .userId(1L)
                .build();
    }

    @Test
    public void testCreateCustomFoodProduct() {
        try (MockedStatic<CustomFoodProductTransformer> mockedTransformer = mockStatic(CustomFoodProductTransformer.class)) {
            // Setup static mocks
            mockedTransformer.when(() -> CustomFoodProductTransformer.fromDto(any(CustomFoodProductDTO.class), any(User.class)))
                    .thenReturn(customFoodProduct);
            mockedTransformer.when(() -> CustomFoodProductTransformer.toDto(any(CustomFoodProduct.class)))
                    .thenReturn(customFoodProductDTO);

            // Arrange
            when(userRepository.existsById(anyLong())).thenReturn(true);
            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
            when(customFoodProductRepository.save(any(CustomFoodProduct.class))).thenReturn(customFoodProduct);

            // Act
            CustomFoodProductDTO result = customFoodProductService.createCustomFoodProduct(customFoodProductDTO);

            mockedTransformer.verify(() -> CustomFoodProductTransformer.fromDto(any(CustomFoodProductDTO.class), any(User.class)));
            mockedTransformer.verify(() -> CustomFoodProductTransformer.toDto(any(CustomFoodProduct.class)));
            // Assert
            verify(customFoodProductRepository).save(any(CustomFoodProduct.class));
            assertEquals(customFoodProductDTO, result);
        }
    }

    @Test
    public void testCreateCustomFoodProductUserNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        Exception thrown = assertThrows(RuntimeException.class,
                () -> customFoodProductService.createCustomFoodProduct(customFoodProductDTOWithoutUser));
        assertTrue(thrown.getMessage().contains("User does not exist"));
    }

    @Test
    public void testUpdateCustomFoodProduct() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(customFoodProductRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(customFoodProductRepository.save(any(CustomFoodProduct.class))).thenReturn(customFoodProduct);
        customFoodProductDTO.setProduct_name("Changed");

        // Act
        CustomFoodProductDTO result = customFoodProductService.updateCustomFoodProduct(customFoodProductDTO);

        // Assert
        verify(customFoodProductRepository, times(1)).save(any(CustomFoodProduct.class));
    }

    @Test
    public void testUpdateCustomFoodProductNotFound() {
        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            customFoodProductService.updateCustomFoodProduct(customFoodProductDTO);
        });

        assertEquals("User with id 1 does not exist", thrown.getMessage());
    }

    @Test
    public void testGetCustomFoodProduct() {
        // Arrange
        when(customFoodProductRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(customFoodProductRepository.findByUserIdAndId(1L, 1L)).thenReturn(customFoodProduct);

        // Act
        CustomFoodProductDTO result = customFoodProductService.getCustomFoodProduct(1L, 1L);

        // Assert
        verify(customFoodProductRepository, times(1)).findByUserIdAndId(1L, 1L);
    }

    @Test
    public void testGetCustomFoodProductNotFound() {
        // Arrange
        when(customFoodProductRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            customFoodProductService.getCustomFoodProduct(1L, 1L);
        });

        assertEquals("Food product with id: 1 does not exist or user does not exist with id: 1", thrown.getMessage());
    }

    @Test
    public void testGetAllCustomFoodProductsByUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(customFoodProductRepository.findAllByUserId(1L)).thenReturn(List.of(customFoodProduct));

        // Act
        List<CustomFoodProductDTO> result = customFoodProductService.getAllCustomFoodProductsByUser(1L);

        // Assert
        verify(customFoodProductRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    public void testGetAllCustomFoodProductsByUserNotFound() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            customFoodProductService.getAllCustomFoodProductsByUser(1L);
        });

        assertEquals("User with id 1 does not exist", thrown.getMessage());
    }

    @Test
    public void testDeleteCustomFoodProduct() {
        // Arrange
        when(customFoodProductRepository.existsById(1L)).thenReturn(true);

        // Act
        customFoodProductService.deleteCustomFoodProduct(1L);

        // Assert
        verify(customFoodProductRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCustomFoodProductNotFound() {
        // Arrange
        when(customFoodProductRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            customFoodProductService.deleteCustomFoodProduct(1L);
        });

        assertEquals("Custom food product with id: 1 does not exist", thrown.getMessage());
    }

}
