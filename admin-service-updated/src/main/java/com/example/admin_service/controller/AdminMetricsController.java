package com.example.admin_service.controller;

import com.example.admin_service.dto.response.AdminMetricsResponse;
import com.example.admin_service.service.AdminMetricsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/metrics")
@CrossOrigin(origins = "*")
public class AdminMetricsController {
    private final AdminMetricsService adminMetricsService;

    public AdminMetricsController(AdminMetricsService adminMetricsService) {
        this.adminMetricsService = adminMetricsService;
    }

    @GetMapping
    public AdminMetricsResponse getMetrics() {
        return adminMetricsService.getMetrics();
    }
}
