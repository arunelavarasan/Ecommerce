package com.example.admin_service.dto.response;

public class AdminMetricsResponse {
    private double totalRefundsProcessed;
    private long pendingRefundsCount;
    private long totalAuditLogs;

    public AdminMetricsResponse(double totalRefundsProcessed, long pendingRefundsCount, long totalAuditLogs) {
        this.totalRefundsProcessed = totalRefundsProcessed;
        this.pendingRefundsCount = pendingRefundsCount;
        this.totalAuditLogs = totalAuditLogs;
    }
    public double getTotalRefundsProcessed() { return totalRefundsProcessed; }
    public long getPendingRefundsCount() { return pendingRefundsCount; }
    public long getTotalAuditLogs() { return totalAuditLogs; }
}
