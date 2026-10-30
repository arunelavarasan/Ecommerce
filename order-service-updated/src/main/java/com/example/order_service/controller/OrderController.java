package com.example.order_service.controller;

import com.example.order_service.dto.request.CancelOrderRequest;
import com.example.order_service.dto.request.CheckoutRequest;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Keeps the existing query-parameter checkout API contract.
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestParam("userId") Long userId,
            @RequestParam("productId") String productId,
            @RequestParam("quantity") String quantity,
            @RequestParam(value = "recipientName", required = false, defaultValue = "Valued Customer") String recipientName,
            @RequestParam(value = "recipientPhone", required = false, defaultValue = "N/A") String recipientPhone,
            @RequestParam(value = "address", required = false, defaultValue = "Standard Delivery Address") String address,
            @RequestParam(value = "paymentMethod", required = false, defaultValue = "CARD") String paymentMethod) {

        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(userId);
        request.setProductId(productId);
        request.setQuantity(quantity);
        request.setRecipientName(recipientName);
        request.setRecipientPhone(recipientPhone);
        request.setAddress(address);
        request.setPaymentMethod(paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @PathVariable String orderId,
            @RequestBody CancelOrderRequest request) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, request.getReason()));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam("status") String newStatus,
            @RequestParam(value = "courierPartner", required = false) String courierPartner) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, newStatus, courierPartner));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{orderId}/attempt-failed")
    public ResponseEntity<Map<String, Object>> recordFailedAttempt(
            @PathVariable String orderId,
            @RequestParam("reason") String reason) {
        return ResponseEntity.ok(orderService.recordFailedAttempt(orderId, reason));
    }

    @GetMapping("/partner/{partnerName}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCourierPartner(
            @PathVariable("partnerName") String partnerName) {
        return ResponseEntity.ok(orderService.getOrdersByCourierPartner(partnerName));
    }
}
