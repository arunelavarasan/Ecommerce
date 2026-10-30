package com.example.product_service.service;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.response.ProductResponse;
import com.example.product_service.dto.response.StockDeductionResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(String id);
    ProductResponse createProduct(CreateProductRequest request);
    StockDeductionResponse deductStock(String id, int quantity);
    String restoreStock(String id, int quantity);
    List<ProductResponse> getLowStockAlerts();
    boolean deleteProduct(String id);
}
