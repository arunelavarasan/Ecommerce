package com.example.user_service.service;

import com.example.user_service.dto.response.CartFavoriteResponse;
import java.util.List;

public interface CartFavoriteService {
    List<CartFavoriteResponse> getItems(Long userId, String itemType);
    boolean exists(Long userId, String productId, String itemType);
    CartFavoriteResponse syncItem(Long userId, String productId, int quantity, String itemType);
    void removeItem(Long userId, String productId, String itemType);
}
