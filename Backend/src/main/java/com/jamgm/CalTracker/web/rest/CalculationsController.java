package com.jamgm.CalTracker.web.rest;

import com.jamgm.CalTracker.service.CalorieCalculationsService;
import com.jamgm.CalTracker.web.rest.DTO.BasalMetabolicRateDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculate")
public class CalculationsController {
    private CalorieCalculationsService calculationsService;

    public CalculationsController(CalorieCalculationsService calculationsService){
        this.calculationsService = calculationsService;
    }

    @PostMapping(value = "/bmr")
    public ResponseEntity<Double> getBasalMetabolicRate(@RequestBody @Valid BasalMetabolicRateDTO dto){
        if(dto.getActivity() != 0){
            return new ResponseEntity<>(this.calculationsService.BasalMetabolicRateWithActivityLevel
                    (dto.getWeight(), dto.getHeight(), dto.getAge(), dto.isMale(), dto.getActivity()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(this.calculationsService.BasalMetabolicRate
                    (dto.getWeight(), dto.getHeight(), dto.getAge(), dto.isMale()), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/calPerDay")
    public ResponseEntity<?> getDailyCaloricIntakePerDay(@RequestBody @Valid BasalMetabolicRateDTO dto){
        if(dto.getUserId() != 0){
            return new ResponseEntity<>(this.calculationsService.DailyCaloricIntakePerDay
                    (dto.getUserId(), dto.getWeight(), dto.getHeight(), dto.getAge(), dto.isMale(), dto.getActivity()),
                    HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Invalid user id", HttpStatus.BAD_REQUEST);
        }
    }
}
