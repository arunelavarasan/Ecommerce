package com.example.admin_service.service.impl;

import com.example.admin_service.dto.request.CreateAdminLogRequest;
import com.example.admin_service.dto.response.AdminLogResponse;
import com.example.admin_service.entity.AdminLog;
import com.example.admin_service.repository.AdminRepository;
import com.example.admin_service.service.AdminLogService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminLogServiceImpl implements AdminLogService {
    private final AdminRepository adminRepository;

    public AdminLogServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminLogResponse createLog(CreateAdminLogRequest request) {
        AdminLog log = new AdminLog(
            request.getUsername(), defaultValue(request.getModule(), "GENERAL"),
            request.getAction(), defaultValue(request.getIpAddress(), "127.0.0.1"));
        return toResponse(adminRepository.save(log));
    }

    @Override
    public List<AdminLogResponse> getAllLogs() {
        return adminRepository.findAll().stream().map(this::toResponse).toList();
    }

    private AdminLogResponse toResponse(AdminLog log) {
        return new AdminLogResponse(log.getId(), log.getAdminUsername(), log.getActionModule(),
                log.getActionMessage(), log.getIpAddress(), log.getTimestamp());
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
