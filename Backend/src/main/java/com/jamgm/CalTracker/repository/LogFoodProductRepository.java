package com.jamgm.CalTracker.repository;

import com.jamgm.CalTracker.model.LogFoodProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LogFoodProductRepository extends JpaRepository<LogFoodProduct, Long> {
    List<LogFoodProduct> findAllByDateAndUserId(LocalDate date, Long user_id);
}
