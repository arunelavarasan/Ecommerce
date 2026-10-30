package com.example.order_service.service;

import com.example.order_service.dto.request.CheckoutRequest;
import com.example.order_service.dto.response.OrderResponse;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse placeOrder(CheckoutRequest request);
    Map<String, String> cancelOrder(String orderId, String reason);
    Map<String, Object> updateOrderStatus(String orderId, String status, String courierPartner);
    List<OrderResponse> getOrdersByUserId(Long userId);
    List<OrderResponse> getAllOrders();
    Map<String, Object> recordFailedAttempt(String orderId, String reason);
    List<OrderResponse> getOrdersByCourierPartner(String partnerName);
}
