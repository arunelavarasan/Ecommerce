package com.example.distribution_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipmentStatusResponse {
    private String message;
    private String orderId;
    private String status;
    private String newStatus;
    private String courierPartner;

    public ShipmentStatusResponse() {}
    public ShipmentStatusResponse(String message, String orderId, String status, String newStatus, String courierPartner) {
        this.message = message;
        this.orderId = orderId;
        this.status = status;
        this.newStatus = newStatus;
        this.courierPartner = courierPartner;
    }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public String getCourierPartner() { return courierPartner; }
    public void setCourierPartner(String courierPartner) { this.courierPartner = courierPartner; }
}
