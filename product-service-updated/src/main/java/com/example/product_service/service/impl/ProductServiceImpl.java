package com.example.product_service.service.impl;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.response.ProductResponse;
import com.example.product_service.dto.response.StockDeductionResponse;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.InsufficientStockException;
import com.example.product_service.exception.InvalidStockQuantityException;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String id) {
        return productRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setId(request.getId() == null || request.getId().trim().isEmpty()
                ? "PROD-" + System.currentTimeMillis() : request.getId().trim());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setStockQuantity(request.getStockQuantity() == null ? 10 : request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setIcon(request.getIcon());
        // Stock quantity is the source of truth, as in the original controller.
        product.setOutOfStock(product.getStockQuantity() <= 0);
        return toResponse(productRepository.save(product));
    }

    @Override
    public StockDeductionResponse deductStock(String id, int quantity) {
        validateQuantity(quantity);
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException();
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        Product saved = productRepository.save(product);
        return new StockDeductionResponse(
                "Stock deducted successfully.",
                saved.getStockQuantity(),
                saved.isOutOfStock());
    }

    @Override
    public String restoreStock(String id, int quantity) {
        validateQuantity(quantity);
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);
        return "Stock restored successfully.";
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockAlerts() {
        return productRepository.findByStockQuantityLessThan(5)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public boolean deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidStockQuantityException();
        }
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getIcon(),
                product.isOutOfStock());
    }
}
