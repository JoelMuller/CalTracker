package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.FoodProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodProductRepository extends JpaRepository<FoodProduct, Long> {
}
