package com.example.admin_service.controller;

import com.example.admin_service.dto.request.CreateAdminLogRequest;
import com.example.admin_service.dto.response.AdminLogResponse;
import com.example.admin_service.service.AdminLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/logs")
@CrossOrigin(origins = "*")
public class AdminLogController {
    private final AdminLogService adminLogService;

    public AdminLogController(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @PostMapping
    public ResponseEntity<AdminLogResponse> createLog(@ModelAttribute CreateAdminLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminLogService.createLog(request));
    }

    @GetMapping
    public List<AdminLogResponse> getAllLogs() {
        return adminLogService.getAllLogs();
    }
}
