package com.example.user_service.controller;

import com.example.user_service.dto.response.CartFavoriteResponse;
import com.example.user_service.service.CartFavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TestCartController {
    private final CartFavoriteService cartFavoriteService;

    public TestCartController(CartFavoriteService cartFavoriteService) {
        this.cartFavoriteService = cartFavoriteService;
    }

    // Kept for compatibility: this endpoint intentionally has no /users prefix.
    @GetMapping("/test-fetch-items")
    public ResponseEntity<?> getItems(
            @RequestParam Long userId, @RequestParam String itemType) {
        try {
            return ResponseEntity.ok(cartFavoriteService.getItems(userId, itemType));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database Error: " + ex.getMessage());
        }
    }
}
