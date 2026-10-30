package com.example.admin_service.controller;

import com.example.admin_service.dto.request.CreateRefundRequest;
import com.example.admin_service.dto.response.RefundResponse;
import com.example.admin_service.service.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/refunds")
@CrossOrigin(origins = "*")
public class RefundController {
    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/request")
    public ResponseEntity<RefundResponse> createRefund(@ModelAttribute CreateRefundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(refundService.createRefund(request));
    }

    @GetMapping
    public List<RefundResponse> getAllRefunds() {
        return refundService.getAllRefunds();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveRefund(@PathVariable Long id) {
        RefundResponse refund = refundService.approveRefund(id);
        String message = "Refund of ₹" + refund.getRefundAmount()
                + " processed successfully for Order #" + refund.getOrderId();
        return ResponseEntity.ok(Map.of("message", message));
    }
}
