package com.jamgm.CalTracker.web.rest.transformer;

import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.web.rest.DTO.UserDTO;

public class UserTransformer {
    public static UserDTO toDto(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .weightLossPerWeek(user.getWeightLossPerWeek())
                .build();
    }

    public static User fromDto(UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .weightLossPerWeek(dto.getWeightLossPerWeek())
                .build();
    }
}
