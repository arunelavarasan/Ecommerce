package com.example.distribution_service.dto.response;

import com.example.distribution_service.entity.Distribution;
import java.time.LocalDateTime;

/** API representation intentionally keeps the original entity field names. */
public class DistributionResponse {
    private Long id;
    private String orderId;
    private Long userId;
    private String courierPartner;
    private String trackingNumber;
    private String recipientCity;
    private String recipientPincode;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime estimatedDelivery;

    public static DistributionResponse from(Distribution d) {
        DistributionResponse r = new DistributionResponse();
        r.setId(d.getId());
        r.setOrderId(d.getOrderId());
        r.setUserId(d.getUserId());
        r.setCourierPartner(d.getCourierPartner());
        r.setTrackingNumber(d.getTrackingNumber());
        r.setRecipientCity(d.getRecipientCity());
        r.setRecipientPincode(d.getRecipientPincode());
        r.setStatus(d.getStatus());
        r.setCreatedAt(d.getCreatedAt());
        r.setDispatchedAt(d.getDispatchedAt());
        r.setDeliveredAt(d.getDeliveredAt());
        r.setEstimatedDelivery(d.getEstimatedDelivery());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCourierPartner() { return courierPartner; }
    public void setCourierPartner(String courierPartner) { this.courierPartner = courierPartner; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getRecipientCity() { return recipientCity; }
    public void setRecipientCity(String recipientCity) { this.recipientCity = recipientCity; }
    public String getRecipientPincode() { return recipientPincode; }
    public void setRecipientPincode(String recipientPincode) { this.recipientPincode = recipientPincode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public LocalDateTime getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(LocalDateTime estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
}
