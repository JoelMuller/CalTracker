package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.config.TestApplicationContext;
import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.service.OpenFoodFactsApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = TestApplicationContext.class)
@ActiveProfiles("test")
public class CustomFoodProductRepositoryTest {
    @Autowired
    private CustomFoodProductRepository customFoodProductRepository;
    @MockBean
    private OpenFoodFactsApiService openFoodFactsApiService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach(){
        User user1 = User.builder()
                .name("testuser1")
                .email("test1@test.com")
                .password("test")
                .weightLossPerWeek(0.50)
                .build();
        CustomFoodProduct customFoodProduct1 = CustomFoodProduct.builder()
                .product_name("testProduct1")
                .serving_size("10g")
                .user(user1)
                .build();
        Nutriments nutriments1 = Nutriments.builder()
                .energyKcal100g(10.0)
                .proteins100g(10.0)
                .carbohydrates100g(10.0)
                .sugars100g(10.0)
                .fat100g(10.0)
                .saturatedFat100g(10.0)
                .fiber100g(10.0)
                .sodium100g(10.0)
                .customFoodProduct(customFoodProduct1)
                .build();
        customFoodProduct1.setNutriments(nutriments1);
        CustomFoodProduct customFoodProduct2 = CustomFoodProduct.builder()
                .product_name("testProduct1")
                .serving_size("10g")
                .user(user1)
                .build();
        Nutriments nutriments = Nutriments.builder()
                .energyKcal100g(10.0)
                .proteins100g(10.0)
                .carbohydrates100g(10.0)
                .sugars100g(10.0)
                .fat100g(10.0)
                .saturatedFat100g(10.0)
                .fiber100g(10.0)
                .sodium100g(10.0)
                .customFoodProduct(customFoodProduct2)
                .build();
        customFoodProduct2.setNutriments(nutriments);
        user1.setCustomFoodProducts(List.of(customFoodProduct1, customFoodProduct2));
        userRepository.save(user1);
        customFoodProductRepository.save(customFoodProduct1);
        customFoodProductRepository.save(customFoodProduct2);
    }

    @Test
    public void testFindAllCustomFoodProductByUserId(){
        User user = userRepository.findByName("testuser1");
        List<CustomFoodProduct> customFoodProducts = user.getCustomFoodProducts();

        assertEquals(customFoodProductRepository.findAllByUserId(user.getId()), customFoodProducts);
    }

    @Test
    public void testCreateCustomFoodProduct(){
        User user = userRepository.findByName("testuser1");
        CustomFoodProduct customFoodProduct = CustomFoodProduct.builder()
                .product_name("newCustomFoodProduct")
                .serving_size("50g")
                .user(user)
                .build();
        Nutriments nutriments = Nutriments.builder()
                .energyKcal100g(10.0)
                .proteins100g(10.0)
                .carbohydrates100g(10.0)
                .sugars100g(10.0)
                .fat100g(10.0)
                .saturatedFat100g(10.0)
                .fiber100g(10.0)
                .sodium100g(10.0)
                .customFoodProduct(customFoodProduct)
                .build();
        customFoodProduct.setNutriments(nutriments);

        int oldSize = customFoodProductRepository.findAll().size();
        customFoodProductRepository.save(customFoodProduct);
        assertNotEquals(customFoodProductRepository.findAll().size(), oldSize);
    }

    @Test
    public void testUpdateCustomFoodProductRepository(){
        CustomFoodProduct customFoodProduct = customFoodProductRepository.findAll().get(0);
        String oldServingSize = customFoodProduct.getServing_size();
        customFoodProduct.setServing_size("50g");
        customFoodProductRepository.save(customFoodProduct);

        assertNotEquals(customFoodProductRepository.findById(customFoodProduct.getId()).get().getServing_size(), oldServingSize);
    }

    @Test
    public void testDeleteCustomFoodProductRepository(){
        CustomFoodProduct customFoodProduct = customFoodProductRepository.findAll().get(0);
        int oldSize = customFoodProductRepository.findAll().size();
        customFoodProductRepository.delete(customFoodProduct);

        assertNotEquals(customFoodProductRepository.findAll().size(), oldSize);
    }
}
