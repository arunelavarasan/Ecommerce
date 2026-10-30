package com.example.user_service.controller;

import com.example.user_service.dto.request.RegisterUserRequest;
import com.example.user_service.dto.response.CartFavoriteResponse;
import com.example.user_service.dto.response.UserResponse;
import com.example.user_service.service.CartFavoriteService;
import com.example.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final CartFavoriteService cartFavoriteService;

    public UserController(UserService userService, CartFavoriteService cartFavoriteService) {
        this.userService = userService;
        this.cartFavoriteService = cartFavoriteService;
    }

    @GetMapping("/cart-favorites-list/{userId}/{itemType}")
    public ResponseEntity<?> getItemsPathVariable(
            @PathVariable Long userId, @PathVariable String itemType) {
        try {
            return ResponseEntity.ok(cartFavoriteService.getItems(userId, itemType.toUpperCase().trim()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB Error: " + ex.getMessage());
        }
    }

    @RequestMapping(value = "/get-items", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> fallbackGetItems(
            @RequestParam Long userId, @RequestParam String itemType) {
        try {
            return ResponseEntity.ok(cartFavoriteService.getItems(userId, itemType));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB Error: " + ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
        } catch (com.example.user_service.exception.ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage() == null ? "Unexpected error" : ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Map<String, String>> blockUser(@PathVariable Long id, @RequestParam String reason) {
        return ResponseEntity.ok(userService.blockUser(id, reason));
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Map<String, String>> unblockUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.unblockUser(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(userService.login(email, password));
    }

    @PostMapping("/sync-item")
    public ResponseEntity<CartFavoriteResponse> syncItem(
            @RequestParam Long userId,
            @RequestParam String productId,
            @RequestParam int quantity,
            @RequestParam String itemType) {
        boolean existed = cartFavoriteService.exists(userId, productId, itemType);
        CartFavoriteResponse response = cartFavoriteService.syncItem(userId, productId, quantity, itemType);
        return ResponseEntity.status(existed ? HttpStatus.OK : HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<Map<String, String>> removeItem(
            @RequestParam Long userId, @RequestParam String productId, @RequestParam String itemType) {
        cartFavoriteService.removeItem(userId, productId, itemType);
        return ResponseEntity.ok(Map.of("message", "Item removed successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserProfile(id));
        } catch (com.example.user_service.exception.ApiException ex) {
            if (ex.getStatus() == HttpStatus.NOT_FOUND) return ResponseEntity.notFound().build();
            throw ex;
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email,
                                           @RequestParam("newPassword") String newPassword) {
        try {
            return ResponseEntity.ok(userService.resetPassword(email, newPassword));
        } catch (com.example.user_service.exception.ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to reset password: " + ex.getMessage()));
        }
    }
}
