package com.example.distribution_service.controller;

import com.example.distribution_service.dto.request.DispatchRequest;
import com.example.distribution_service.dto.request.UpdateShipmentStatusRequest;
import com.example.distribution_service.dto.response.DistributionResponse;
import com.example.distribution_service.dto.response.ShipmentStatusResponse;
import com.example.distribution_service.service.DistributionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distribution")
@CrossOrigin(origins = "*")
public class DistributionController {
    private final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping("/dispatch")
    public ResponseEntity<DistributionResponse> initiateShipment(
            @RequestParam String orderId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "Delivery Partner Not Assigned Yet") String courierPartner,
            @RequestParam(defaultValue = "N/A") String city,
            @RequestParam(defaultValue = "N/A") String pincode) {
        DispatchRequest request = new DispatchRequest();
        request.setOrderId(orderId);
        request.setUserId(userId);
        request.setCourierPartner(courierPartner);
        request.setCity(city);
        request.setPincode(pincode);
        return ResponseEntity.status(HttpStatus.CREATED).body(distributionService.initiateShipment(request));
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<DistributionResponse> getTrackingByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(distributionService.getTrackingByOrder(orderId));
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<ShipmentStatusResponse> updateShipmentStatus(
            @PathVariable String orderId,
            @RequestParam String status,
            @RequestParam(required = false) String courierPartner) {
        return ResponseEntity.ok(distributionService.updateShipmentStatus(
                orderId, new UpdateShipmentStatusRequest(status, courierPartner), true));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DistributionResponse>> filterShipments(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String pincode) {
        return ResponseEntity.ok(distributionService.filterShipments(city, pincode));
    }

    @GetMapping
    public ResponseEntity<List<DistributionResponse>> getAllShipments() {
        return ResponseEntity.ok(distributionService.getAllShipments());
    }

    @GetMapping("/partner/{partnerName}")
    public ResponseEntity<List<DistributionResponse>> getOrdersByPartner(@PathVariable String partnerName) {
        return ResponseEntity.ok(distributionService.getShipmentsByPartner(partnerName));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ShipmentStatusResponse> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam("status") String status) {
        return ResponseEntity.ok(distributionService.updateShipmentStatus(
                orderId, new UpdateShipmentStatusRequest(status, null), false));
    }
}
