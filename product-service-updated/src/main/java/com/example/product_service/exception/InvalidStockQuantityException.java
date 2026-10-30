package com.example.product_service.exception;

public class InvalidStockQuantityException extends RuntimeException {
    public InvalidStockQuantityException() {
        super("Quantity must be greater than zero.");
    }
}
