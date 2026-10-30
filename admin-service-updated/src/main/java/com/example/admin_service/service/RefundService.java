package com.example.admin_service.service;

import com.example.admin_service.dto.request.CreateRefundRequest;
import com.example.admin_service.dto.response.RefundResponse;
import java.util.List;

public interface RefundService {
    RefundResponse createRefund(CreateRefundRequest request);
    List<RefundResponse> getAllRefunds();
    RefundResponse approveRefund(Long id);
}
