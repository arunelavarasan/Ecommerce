package com.example.admin_service.service.impl;

import com.example.admin_service.dto.response.AdminMetricsResponse;
import com.example.admin_service.entity.RefundRecord;
import com.example.admin_service.repository.AdminRepository;
import com.example.admin_service.repository.RefundRepository;
import com.example.admin_service.service.AdminMetricsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminMetricsServiceImpl implements AdminMetricsService {
    private final AdminRepository adminRepository;
    private final RefundRepository refundRepository;

    public AdminMetricsServiceImpl(AdminRepository adminRepository, RefundRepository refundRepository) {
        this.adminRepository = adminRepository;
        this.refundRepository = refundRepository;
    }

    @Override
    public AdminMetricsResponse getMetrics() {
        List<RefundRecord> processed = refundRepository.findByStatus("PROCESSED");
        double total = processed.stream().mapToDouble(RefundRecord::getRefundAmount).sum();
        long pending = refundRepository.findByStatus("PENDING").size();
        return new AdminMetricsResponse(total, pending, adminRepository.count());
    }
}
