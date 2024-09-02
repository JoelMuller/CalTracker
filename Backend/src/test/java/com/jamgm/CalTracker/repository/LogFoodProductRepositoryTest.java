package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.model.LogFoodProduct;
import com.jamgm.CalTracker.model.Nutriments;
import com.jamgm.CalTracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LogFoodProductRepositoryTest {
    @Autowired
    private LogFoodProductRepository logFoodProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomFoodProductRepository customFoodProductRepository;

    @BeforeEach
    public void beforeEach() {
        User user1 = User.builder()
                .name("testuser1")
                .email("test1@test.com")
                .password("test")
                .weightLossPerWeek(0.50)
                .build();
        CustomFoodProduct customFoodProduct = CustomFoodProduct.builder()
                .product_name("testProduct")
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
                .customFoodProduct(customFoodProduct)
                .build();
        customFoodProduct.setNutriments(nutriments);
        user1.setCustomFoodProducts(List.of(customFoodProduct));
        LogFoodProduct logFoodProductBarcode1 = LogFoodProduct.builder()
                .foodProductBarcode("123")
                .date(LocalDate.of(2024, 1, 1))
                .user(user1)
                .build();
        LogFoodProduct logFoodProductBarcode2 = LogFoodProduct.builder()
                .foodProductBarcode("1234")
                .date(LocalDate.of(2024, 1, 2))
                .user(user1)
                .build();
        LogFoodProduct logFoodProductCustom = LogFoodProduct.builder()
                .date(LocalDate.of(2024, 1, 1))
                .user(user1)
                .customFoodProduct(customFoodProduct)
                .build();
        user1.setLoggedFoodProducts(List.of(logFoodProductBarcode1, logFoodProductBarcode2, logFoodProductCustom));

        userRepository.save(user1);
        customFoodProductRepository.save(customFoodProduct);
        logFoodProductRepository.save(logFoodProductBarcode1);
        logFoodProductRepository.save(logFoodProductBarcode2);
        logFoodProductRepository.save(logFoodProductCustom);
    }

    @Test
    public void testFindAllLogFoodProductByDateAndUserId(){
        long userid = userRepository.findByName("testuser1").getId();
        userRepository.findByName("testuser1").getLoggedFoodProducts().forEach(product -> System.out.println(product.getDate()));
        List<LogFoodProduct> logFoodProducts = logFoodProductRepository.
                findAllByDateAndUserId(LocalDate.of(2024, 1, 1), userid);

        assertEquals(logFoodProducts.size(), 2);
    }

    @Test
    public void testCreateLogFoodProduct(){
        User user = userRepository.findByName("testuser1");
        LogFoodProduct logFoodProduct = LogFoodProduct.builder()
                .foodProductBarcode("987")
                .date(LocalDate.of(2024, 2, 2))
                .user(user)
                .build();
        int size = logFoodProductRepository.findAll().size();
        logFoodProductRepository.save(logFoodProduct);

        assertNotEquals(logFoodProductRepository.findAll().size(), size);
    }

    @Test
    public void testUpdateLogFoodProduct(){
        LogFoodProduct logFoodProduct = logFoodProductRepository.findAll().get(0);
        String oldBarcode = logFoodProduct.getFoodProductBarcode();
        logFoodProduct.setFoodProductBarcode("111");
        logFoodProductRepository.save(logFoodProduct);

        assertNotEquals(logFoodProductRepository.findAll().get(0).getFoodProductBarcode(), oldBarcode);
    }

    @Test
    public void testDeleteLogFoodProduct(){
        int oldSize = logFoodProductRepository.findAll().size();
        LogFoodProduct logFoodProduct = logFoodProductRepository.findAll().get(0);
        logFoodProductRepository.delete(logFoodProduct);

        assertNotEquals(logFoodProductRepository.findAll().size(), oldSize);
    }
}
