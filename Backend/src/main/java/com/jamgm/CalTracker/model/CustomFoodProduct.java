package com.jamgm.CalTracker.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomFoodProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String product_name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutriments_id")
    private Nutriments nutriments;
    private String serving_size;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
