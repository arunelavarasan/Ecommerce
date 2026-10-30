package com.example.distribution_service.service.impl;

import com.example.distribution_service.dto.request.DispatchRequest;
import com.example.distribution_service.dto.request.UpdateShipmentStatusRequest;
import com.example.distribution_service.dto.response.DistributionResponse;
import com.example.distribution_service.dto.response.ShipmentStatusResponse;
import com.example.distribution_service.entity.Distribution;
import com.example.distribution_service.exception.ResourceNotFoundException;
import com.example.distribution_service.exception.InvalidRequestException;
import com.example.distribution_service.repository.DistributionRepository;
import com.example.distribution_service.service.DistributionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DistributionServiceImpl implements DistributionService {
    private final DistributionRepository repository;

    public DistributionServiceImpl(DistributionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public DistributionResponse initiateShipment(DispatchRequest request) {
        if (request == null || isBlank(request.getOrderId()) || request.getUserId() == null) {
            throw new InvalidRequestException("orderId and userId are required.");
        }
        String tracking = "TRK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        String courier = isBlank(request.getCourierPartner())
                ? "Delivery Partner Not Assigned Yet" : request.getCourierPartner().trim();
        String city = isBlank(request.getCity()) ? "N/A" : request.getCity().trim();
        String pincode = isBlank(request.getPincode()) ? "N/A" : request.getPincode().trim();

        Distribution shipment = new Distribution(request.getOrderId().trim(), request.getUserId(),
                courier, tracking, city, pincode, "PACKING");
        return DistributionResponse.from(repository.save(shipment));
    }

    @Override
    @Transactional(readOnly = true)
    public DistributionResponse getTrackingByOrder(String orderId) {
        if (isBlank(orderId)) throw new InvalidRequestException("orderId is required.");
        Distribution shipment = repository.findByOrderId(orderId.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tracking details not found for Order ID: " + orderId.trim()));
        return DistributionResponse.from(shipment);
    }

    @Override
    @Transactional
    public ShipmentStatusResponse updateShipmentStatus(String orderId, UpdateShipmentStatusRequest request, boolean includePartner) {
        if (request == null || isBlank(request.getStatus())) {
            throw new InvalidRequestException("status is required.");
        }
        if (isBlank(orderId)) throw new InvalidRequestException("orderId is required.");
        Distribution shipment = repository.findByOrderId(orderId.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        includePartner
                                ? "Shipment record not found for Order ID: " + orderId.trim()
                                : "Shipment not found for Order ID: " + orderId.trim()));
        String normalizedStatus = request.getStatus().trim().toUpperCase(Locale.ROOT);
        shipment.setStatus(normalizedStatus);

        if (includePartner && request.getCourierPartner() != null && !request.getCourierPartner().trim().isEmpty()) {
            shipment.setCourierPartner(request.getCourierPartner().trim());
        }
        updateStageTimestamps(shipment, normalizedStatus);
        repository.save(shipment);

        if (includePartner) {
            return new ShipmentStatusResponse("Shipment stage updated successfully.", orderId,
                    null, normalizedStatus, shipment.getCourierPartner());
        }
        return new ShipmentStatusResponse(
                "Order shipment status updated successfully to " + normalizedStatus,
                orderId, normalizedStatus, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponse> filterShipments(String city, String pincode) {
        List<Distribution> result;
        if (!isBlank(city)) {
            result = repository.findByRecipientCityIgnoreCase(city.trim());
        } else if (!isBlank(pincode)) {
            result = repository.findByRecipientPincode(pincode.trim());
        } else {
            result = repository.findAll();
        }
        return result.stream().map(DistributionResponse::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponse> getAllShipments() {
        return repository.findAll().stream().map(DistributionResponse::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponse> getShipmentsByPartner(String partnerName) {
        if (isBlank(partnerName)) return List.of();
        String query = partnerName.trim().toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(d -> d.getCourierPartner() != null &&
                        d.getCourierPartner().toLowerCase(Locale.ROOT).contains(query))
                .map(DistributionResponse::from)
                .collect(Collectors.toList());
    }

    private Distribution findByOrderId(String orderId) {
        if (isBlank(orderId)) throw new InvalidRequestException("orderId is required.");
        return repository.findByOrderId(orderId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Tracking details not found for Order ID: " + orderId.trim()));
    }

    private void updateStageTimestamps(Distribution shipment, String status) {
        LocalDateTime now = LocalDateTime.now();
        if ("DISPATCHED".equals(status)) shipment.setDispatchedAt(now);
        if ("DELIVERED".equals(status)) shipment.setDeliveredAt(now);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
