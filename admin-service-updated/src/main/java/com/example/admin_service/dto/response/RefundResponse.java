package com.example.admin_service.dto.response;

import java.time.LocalDateTime;

public class RefundResponse {
    private Long id;
    private String orderId;
    private Long userId;
    private double refundAmount;
    private String paymentMethod;
    private String cancellationReason;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;

    public RefundResponse(Long id, String orderId, Long userId, double amount,
                          String paymentMethod, String reason, String status,
                          LocalDateTime requestedAt, LocalDateTime processedAt) {
        this.id = id; this.orderId = orderId; this.userId = userId; this.refundAmount = amount;
        this.paymentMethod = paymentMethod; this.cancellationReason = reason; this.status = status;
        this.requestedAt = requestedAt; this.processedAt = processedAt;
    }
    public Long getId() { return id; }
    public String getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
    public double getRefundAmount() { return refundAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getCancellationReason() { return cancellationReason; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}
