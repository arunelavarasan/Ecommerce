package com.example.distribution_service.dto.request;

public class UpdateShipmentStatusRequest {
    private String status;
    private String courierPartner;

    public UpdateShipmentStatusRequest() {}
    public UpdateShipmentStatusRequest(String status, String courierPartner) {
        this.status = status;
        this.courierPartner = courierPartner;
    }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCourierPartner() { return courierPartner; }
    public void setCourierPartner(String courierPartner) { this.courierPartner = courierPartner; }
}
