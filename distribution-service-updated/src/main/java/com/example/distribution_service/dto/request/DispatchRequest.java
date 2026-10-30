package com.example.distribution_service.dto.request;

/** Input for creating a shipment. Defaults preserve the legacy dispatch endpoint behavior. */
public class DispatchRequest {
    private String orderId;
    private Long userId;
    private String courierPartner = "Delivery Partner Not Assigned Yet";
    private String city = "N/A";
    private String pincode = "N/A";

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCourierPartner() { return courierPartner; }
    public void setCourierPartner(String courierPartner) { this.courierPartner = courierPartner; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}
