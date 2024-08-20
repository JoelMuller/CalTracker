package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}
