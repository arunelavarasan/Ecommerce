package com.example.order_service.dto.response;

import com.example.order_service.entity.Order;
import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private String orderId;
    private Long userId;
    private String productId;
    private String quantity;
    private double totalBill;
    private String status;
    private String cancelReason;
    private String courierPartner;
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private String deliveryOtp;
    private int deliveryAttempts;

    public static OrderResponse from(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.id = order.getId();
        dto.orderId = order.getOrderId();
        dto.userId = order.getUserId();
        dto.productId = order.getProductId();
        dto.quantity = order.getQuantity();
        dto.totalBill = order.getTotalBill();
        dto.status = order.getStatus();
        dto.cancelReason = order.getCancelReason();
        dto.courierPartner = order.getCourierPartner();
        dto.recipientName = order.getRecipientName();
        dto.recipientPhone = order.getRecipientPhone();
        dto.shippingAddress = order.getShippingAddress();
        dto.paymentMethod = order.getPaymentMethod();
        dto.orderDate = order.getOrderDate();
        dto.deliveryOtp = order.getDeliveryOtp();
        dto.deliveryAttempts = order.getDeliveryAttempts();
        return dto;
    }

    public Long getId() { return id; }
    public String getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
    public String getProductId() { return productId; }
    public String getQuantity() { return quantity; }
    public double getTotalBill() { return totalBill; }
    public String getStatus() { return status; }
    public String getCancelReason() { return cancelReason; }
    public String getCourierPartner() { return courierPartner; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientPhone() { return recipientPhone; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getDeliveryOtp() { return deliveryOtp; }
    public int getDeliveryAttempts() { return deliveryAttempts; }
}
