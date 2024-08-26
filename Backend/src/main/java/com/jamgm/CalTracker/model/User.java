package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private double weightLossPerWeek; //amount of kg's user wants to lose per week

    @Builder
    public User(long id, String name, String email, String password, double weightLossPerWeek){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.weightLossPerWeek = weightLossPerWeek;
    }
}
