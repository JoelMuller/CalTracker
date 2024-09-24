package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log_food_product")
public class LogFoodProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String productName;
    private String foodProductBarcode;
    @ManyToOne(optional = true)
    private CustomFoodProduct customFoodProduct;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
