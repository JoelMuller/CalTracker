package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String password;
    private double weight;
    private int basalMetabolicRate;
    private double weightLossPerWeek; //amount of kg's user wants to lose per week
    @OneToMany
    @JoinColumn(nullable = true)
    private List<LogFoodProduct> loggedFoodProducts;
    @OneToMany
    @JoinColumn(nullable = true)
    private List<CustomFoodProduct> customFoodProducts;

    @Builder
    public User(long id, String name, String email, String password, double weight, int basalMetabolicRate, double weightLossPerWeek, List<LogFoodProduct> loggedFoodProducts, List<CustomFoodProduct> customFoodProducts){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.weight = weight;
        this.basalMetabolicRate = basalMetabolicRate;
        this.weightLossPerWeek = weightLossPerWeek;
        this.loggedFoodProducts = loggedFoodProducts;
        this.customFoodProducts = customFoodProducts;
    }
}
