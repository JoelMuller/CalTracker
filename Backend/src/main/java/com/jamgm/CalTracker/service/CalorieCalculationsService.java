package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.User;
import com.jamgm.CalTracker.repository.UserRepository;

public class CalorieCalculationsService {
    private UserService userService;

    public CalorieCalculationsService(UserService userService){
        this.userService = userService;
    }

    public double BasalMetabolicRate(double weight, double height, int age, boolean male){
        //using the Mifflin-St Jeor equation calculates how much calories you burn in a day
        if(male){
            return 10 * weight + 6.25 * height - 5 * age + 5;
        }else{
            return 10 * weight + 6.25 * height - 5 * age - 161;
        }
    }
    public double BasalMetabolicRateWithActivityLevel(double weight, double height, int age, boolean male, double activity){
        //using the Mifflin-St Jeor equation calculates how much calories you burn in a day also accounting for amount
        //of exercise
        //activity value needs to be sent from the front-end
        if(male){
            return (10 * weight + 6.25 * height - 5 * age + 5) * activity;
        }else{
            return (10 * weight + 6.25 * height - 5 * age - 161) * activity;
        }
    }

    public double DailyCaloricIntakePerDay(long userId, double basalMetabolicRate){
        User user = userService.getUserById(userId).get();
        double caloricDeficitPerDay = user.getWeightLossPerWeek() * 7700 / 7;
        return basalMetabolicRate - caloricDeficitPerDay;
    }
}
