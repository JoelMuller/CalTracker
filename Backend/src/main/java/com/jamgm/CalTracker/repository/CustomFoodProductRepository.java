package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomFoodProductRepository extends JpaRepository<CustomFoodProduct, Long> {
    List<CustomFoodProduct> findAllByUserId(Long user_id);
    CustomFoodProduct findByUserIdAndId(Long user_id, Long id);
}
