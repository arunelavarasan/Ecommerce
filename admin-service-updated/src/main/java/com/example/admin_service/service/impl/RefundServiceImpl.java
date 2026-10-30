package com.example.admin_service.service.impl;

import com.example.admin_service.dto.request.CreateRefundRequest;
import com.example.admin_service.dto.response.RefundResponse;
import com.example.admin_service.entity.RefundRecord;
import com.example.admin_service.repository.RefundRepository;
import com.example.admin_service.service.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;

    public RefundServiceImpl(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    @Override
    @Transactional
    public RefundResponse createRefund(CreateRefundRequest request) {
        RefundRecord record = new RefundRecord(request.getOrderId(), request.getUserId(),
                request.getAmount(), request.getPaymentMethod(), request.getReason());
        return toResponse(refundRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getAllRefunds() {
        return refundRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public RefundResponse approveRefund(Long id) {
        RefundRecord refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Refund record not found."));
        if ("REJECTED".equalsIgnoreCase(refund.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rejected refunds cannot be processed.");
        }
        if (!"PROCESSED".equalsIgnoreCase(refund.getStatus())) {
            refund.setStatus("PROCESSED");
            refund.setProcessedAt(LocalDateTime.now());
        }
        return toResponse(refundRepository.save(refund));
    }

    private RefundResponse toResponse(RefundRecord refund) {
        return new RefundResponse(refund.getId(), refund.getOrderId(), refund.getUserId(),
                refund.getRefundAmount(), refund.getPaymentMethod(), refund.getCancellationReason(),
                refund.getStatus(), refund.getRequestedAt(), refund.getProcessedAt());
    }
}
