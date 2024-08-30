package com.jamgm.CalTracker.service;

import com.jamgm.CalTracker.model.CustomFoodProduct;
import com.jamgm.CalTracker.repository.CustomFoodProductRepository;
import com.jamgm.CalTracker.repository.UserRepository;
import com.jamgm.CalTracker.web.rest.DTO.CustomFoodProductDTO;
import com.jamgm.CalTracker.web.rest.transformer.CustomFoodProductTransformer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomFoodProductService {
    private final CustomFoodProductRepository customFoodProductRepository;
    private final UserRepository userRepository;
    public CustomFoodProductService(CustomFoodProductRepository customFoodProductRepository,
                                    UserRepository userRepository){
        this.userRepository = userRepository;
        this.customFoodProductRepository = customFoodProductRepository;
    }

    public CustomFoodProductDTO createCustomFoodProduct(CustomFoodProductDTO customFoodProductDTO){
        return CustomFoodProductTransformer
                .toDto(this.customFoodProductRepository
                        .save(CustomFoodProductTransformer.fromDto(customFoodProductDTO)));
    }

    public CustomFoodProductDTO updateCustomFoodProduct(CustomFoodProductDTO customFoodProductDTO){
        CustomFoodProduct customFoodProduct = CustomFoodProductTransformer.fromDto(customFoodProductDTO);
        if(customFoodProductRepository.existsById(customFoodProduct.getId())){
            return CustomFoodProductTransformer
                    .toDto(customFoodProductRepository.save(customFoodProduct));
        }else{
            throw new RuntimeException("Food product with id: " + customFoodProduct.getId() + " does not exist");
        }
    }

    public CustomFoodProductDTO getCustomFoodProduct(long userId, long customFoodProductId){
        if(customFoodProductRepository.existsById(customFoodProductId) && userRepository.existsById(userId)){
            return CustomFoodProductTransformer
                    .toDto(customFoodProductRepository.findByUserIdAndId(userId, customFoodProductId));
        }else{
            throw new RuntimeException("Food product with id: " + customFoodProductId + " does not exist or user does not exist with id: " + userId);
        }
    }

    public List<CustomFoodProductDTO> getAllCustomFoodProductsByUser(long userId){
        if(userRepository.existsById(userId)){
            return this.customFoodProductRepository.findAllByUserId(userId).stream()
                    .map(CustomFoodProductTransformer::toDto)
                    .toList();
        }else{
            throw new RuntimeException("No food products found for this user.");

        }
    }

    public void deleteCustomFoodProduct(long customFoodProductId){
        if(customFoodProductRepository.existsById(customFoodProductId)){
            customFoodProductRepository.deleteById(customFoodProductId);
        }else{
            throw new RuntimeException("User with id: " + customFoodProductId + " does not exist");
        }
    }
}
