package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;

public class UserTransformer {
    public static UserDTO toDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .weight(user.getWeight())
                .basalMetabolicRate(user.getBasalMetabolicRate())
                .weightLossPerWeek(user.getWeightLossPerWeek())
                .loggedFoodProducts(user.getLoggedFoodProducts())
                .build();
    }

    public static User fromDto(UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .weight(dto.getWeight())
                .basalMetabolicRate(dto.getBasalMetabolicRate())
                .weightLossPerWeek(dto.getWeightLossPerWeek())
                .loggedFoodProducts(dto.getLoggedFoodProducts())
                .build();
    }
}
