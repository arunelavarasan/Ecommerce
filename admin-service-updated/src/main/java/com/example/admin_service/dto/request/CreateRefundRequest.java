package com.example.admin_service.dto.request;

public class CreateRefundRequest {
    private String orderId;
    private Long userId;
    private double amount;
    private String paymentMethod;
    private String reason;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
