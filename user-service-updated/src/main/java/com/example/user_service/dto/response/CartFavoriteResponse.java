package com.example.user_service.dto.response;

import com.example.user_service.entity.CartFavorite;

public class CartFavoriteResponse {
    private Long id;
    private Long userId;
    private String productId;
    private int quantity;
    private String itemType;

    public static CartFavoriteResponse from(CartFavorite item) {
        CartFavoriteResponse dto = new CartFavoriteResponse();
        dto.id = item.getId();
        dto.userId = item.getUserId();
        dto.productId = item.getProductId();
        dto.quantity = item.getQuantity();
        dto.itemType = item.getItemType();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
}
