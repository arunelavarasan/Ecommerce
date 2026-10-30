package com.example.user_service.service.impl;

import com.example.user_service.dto.response.CartFavoriteResponse;
import com.example.user_service.entity.CartFavorite;
import com.example.user_service.repository.CartFavoriteRepository;
import com.example.user_service.service.CartFavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartFavoriteServiceImpl implements CartFavoriteService {
    private final CartFavoriteRepository cartFavoriteRepository;

    public CartFavoriteServiceImpl(CartFavoriteRepository cartFavoriteRepository) {
        this.cartFavoriteRepository = cartFavoriteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartFavoriteResponse> getItems(Long userId, String itemType) {
        return cartFavoriteRepository.findByUserIdAndItemType(userId, itemType)
                .stream().map(CartFavoriteResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long userId, String productId, String itemType) {
        return cartFavoriteRepository.findByUserIdAndProductIdAndItemType(userId, productId, itemType).isPresent();
    }

    @Override
    public CartFavoriteResponse syncItem(Long userId, String productId, int quantity, String itemType) {
        CartFavorite item = cartFavoriteRepository
                .findByUserIdAndProductIdAndItemType(userId, productId, itemType)
                .orElseGet(() -> new CartFavorite(userId, productId, quantity, itemType));

        if (item.getId() != null) {
            if ("CART".equalsIgnoreCase(itemType)) {
                item.setQuantity(quantity);
            }
            return CartFavoriteResponse.from(cartFavoriteRepository.save(item));
        }
        return CartFavoriteResponse.from(cartFavoriteRepository.save(item));
    }

    @Override
    public void removeItem(Long userId, String productId, String itemType) {
        cartFavoriteRepository.deleteByUserIdAndProductIdAndItemType(userId, productId, itemType);
    }
}
