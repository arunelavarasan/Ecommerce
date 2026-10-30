package com.example.product_service.controller;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.response.ProductResponse;
import com.example.product_service.dto.response.StockDeductionResponse;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // User catalog: list all products.
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    // Retrieve one product.
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (ProductNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin: create a product.
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    // Admin/system: deduct stock during order placement.
    @PutMapping("/{id}/deduct-stock")
    public ResponseEntity<StockDeductionResponse> deductStock(
            @PathVariable String id,
            @RequestParam int quantity) {
        return ResponseEntity.ok(productService.deductStock(id, quantity));
    }

    // Admin/system: restore stock during order cancellation.
    @PutMapping("/{id}/restore-stock")
    public ResponseEntity<String> restoreStock(
            @PathVariable String id,
            @RequestParam int quantity) {
        return ResponseEntity.ok(productService.restoreStock(id, quantity));
    }

    // Admin dashboard: products with stock below five.
    @GetMapping("/alerts/low-stock")
    public List<ProductResponse> getLowStockAlerts() {
        return productService.getLowStockAlerts();
    }

    // Admin: delete a product.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        if (!productService.deleteProduct(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
