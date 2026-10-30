package com.example.distribution_service.service;

import com.example.distribution_service.dto.request.DispatchRequest;
import com.example.distribution_service.dto.request.UpdateShipmentStatusRequest;
import com.example.distribution_service.dto.response.DistributionResponse;
import com.example.distribution_service.dto.response.ShipmentStatusResponse;
import java.util.List;

public interface DistributionService {
    DistributionResponse initiateShipment(DispatchRequest request);
    DistributionResponse getTrackingByOrder(String orderId);
    ShipmentStatusResponse updateShipmentStatus(String orderId, UpdateShipmentStatusRequest request, boolean includePartner);
    List<DistributionResponse> filterShipments(String city, String pincode);
    List<DistributionResponse> getAllShipments();
    List<DistributionResponse> getShipmentsByPartner(String partnerName);
}
