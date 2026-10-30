package com.example.admin_service.service;

import com.example.admin_service.dto.request.CreateAdminLogRequest;
import com.example.admin_service.dto.response.AdminLogResponse;
import java.util.List;

public interface AdminLogService {
    AdminLogResponse createLog(CreateAdminLogRequest request);
    List<AdminLogResponse> getAllLogs();
}
