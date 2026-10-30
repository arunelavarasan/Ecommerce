package com.example.order_service.service.impl;

import com.example.order_service.client.AdminClient;
import com.example.order_service.client.DistributionClient;
import com.example.order_service.client.ProductClient;
import com.example.order_service.client.UserClient;
import com.example.order_service.dto.ProductDTO;
import com.example.order_service.dto.request.CheckoutRequest;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired(required = false)
    private ProductClient productClient;
    @Autowired(required = false)
    private UserClient userClient;
    @Autowired(required = false)
    private DistributionClient distributionClient;
    @Autowired(required = false)
    private AdminClient adminClient;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private String generateFormattedOrderId() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String unique6Digit = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ORD-" + datePrefix + "-" + unique6Digit;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(CheckoutRequest request) {
        if (request.getUserId() == null || request.getProductId() == null || request.getProductId().isBlank()
                || request.getQuantity() == null || request.getQuantity().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId, productId and quantity are required.");
        }

        String[] productIds = request.getProductId().split(",");
        String[] quantities = request.getQuantity().split(",");
        double grandTotal = 0.0;

        for (int i = 0; i < productIds.length; i++) {
            String pId = productIds[i].trim();
            int qty = 1;
            if (i < quantities.length) {
                try { qty = Integer.parseInt(quantities[i].trim()); }
                catch (Exception ignored) { qty = 1; }
            }

            double productPrice = 299.99;
            if (productClient != null) {
                try {
                    ProductDTO product = productClient.getProductById(pId);
                    if (product != null && product.getPrice() > 0) productPrice = product.getPrice();
                } catch (Throwable ignored) { }
            }
            grandTotal += productPrice * qty;
            if (productClient != null) {
                try { productClient.deductStock(pId, qty); }
                catch (Throwable ignored) { }
            }
        }

        String generatedOrderId = generateFormattedOrderId();
        String recipientName = defaultIfBlank(request.getRecipientName(), "Valued Customer");
        Order order = new Order(generatedOrderId, request.getUserId(), request.getProductId(),
                request.getQuantity(), grandTotal, "PLACED", recipientName,
                defaultIfBlank(request.getRecipientPhone(), "N/A"),
                defaultIfBlank(request.getAddress(), "Standard Delivery Address"),
                defaultIfBlank(request.getPaymentMethod(), "CARD"));

        Order savedOrder = orderRepository.save(order);

        if (distributionClient != null) {
            try {
                distributionClient.initiateShipment(generatedOrderId, request.getUserId(),
                        "Delivery Partner Not Assigned Yet", "N/A", "N/A");
            } catch (Throwable ignored) { }
        }
        if (adminClient != null) {
            try {
                adminClient.saveAuditLog(recipientName, "ORDER", "Placed Order #" + generatedOrderId, "127.0.0.1");
            } catch (Throwable ignored) { }
        }
        return OrderResponse.from(savedOrder);
    }

    @Override
    @Transactional
    public Map<String, String> cancelOrder(String orderId, String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reason is mandatory for cancellation.");
        }
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order entry not found."));
        order.setStatus("CANCELLED");
        order.setCancelReason(reason.trim());
        orderRepository.save(order);

        if (distributionClient != null) {
            try { distributionClient.updateShipmentStatus(orderId, "CANCELLED", null); }
            catch (Exception ignored) { }
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order successfully marked as CANCELLED.");
        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> updateOrderStatus(String orderId, String newStatus, String courierPartner) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required.");
        }
        order.setStatus(newStatus.toUpperCase().trim());
        if (courierPartner != null && !courierPartner.trim().isEmpty()) {
            order.setCourierPartner(courierPartner.trim());
        }
        orderRepository.save(order);
        if (distributionClient != null) {
            try { distributionClient.updateShipmentStatus(orderId, newStatus, courierPartner); }
            catch (Exception ignored) { }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Status updated successfully.");
        response.put("currentStatus", order.getStatus());
        response.put("courierPartner", order.getCourierPartner());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> recordFailedAttempt(String orderId, String reason) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
        int attempts = order.getDeliveryAttempts() + 1;
        order.setDeliveryAttempts(attempts);
        if (attempts >= 3) {
            order.setStatus("RTO");
            order.setCancelReason("RTO: 3 Failed Delivery Attempts. Note: " + reason);
        } else {
            order.setStatus("WAITING_FOR_DELIVERY");
            order.setCancelReason("Attempt " + attempts + " Failed: " + reason);
        }
        orderRepository.save(order);
        Map<String, Object> response = new HashMap<>();
        response.put("attempts", attempts);
        response.put("status", order.getStatus());
        response.put("message", "RTO".equals(order.getStatus())
                ? "Order flagged as Return to Origin (RTO)" : "Delivery attempt logged.");
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCourierPartner(String partnerName) {
        if (partnerName == null || partnerName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Courier partner is required.");
        }
        return orderRepository.findByCourierPartnerIgnoreCase(partnerName.trim())
                .stream().map(OrderResponse::from).collect(Collectors.toList());
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
