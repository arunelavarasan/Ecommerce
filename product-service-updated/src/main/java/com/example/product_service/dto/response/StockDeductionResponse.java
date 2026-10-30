package com.example.product_service.dto.response;

public class StockDeductionResponse {
    private String message;
    private int remainingStock;
    private boolean isOutOfStock;

    public StockDeductionResponse(String message, int remainingStock, boolean isOutOfStock) {
        this.message = message;
        this.remainingStock = remainingStock;
        this.isOutOfStock = isOutOfStock;
    }

    public String getMessage() { return message; }
    public int getRemainingStock() { return remainingStock; }
    public boolean isOutOfStock() { return isOutOfStock; }
}
