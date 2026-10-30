package com.example.order_service.dto.request;

public class CancelOrderRequest {
    private String reason;
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
