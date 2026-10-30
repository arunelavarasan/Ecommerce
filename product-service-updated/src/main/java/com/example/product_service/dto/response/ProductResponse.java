package com.example.product_service.dto.response;

public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int stockQuantity;
    private String category;
    private String icon;
    private boolean isOutOfStock;

    public ProductResponse() {}

    public ProductResponse(String id, String name, String description, double price,
                           String imageUrl, int stockQuantity, String category,
                           String icon, boolean isOutOfStock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.icon = icon;
        this.isOutOfStock = isOutOfStock;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public boolean isOutOfStock() { return isOutOfStock; }
    public void setOutOfStock(boolean outOfStock) { isOutOfStock = outOfStock; }
}
