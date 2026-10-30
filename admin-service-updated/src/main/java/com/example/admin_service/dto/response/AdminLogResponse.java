package com.example.admin_service.dto.response;

import java.time.LocalDateTime;

public class AdminLogResponse {
    private Long id;
    private String adminUsername;
    private String actionModule;
    private String actionMessage;
    private String ipAddress;
    private LocalDateTime timestamp;

    public AdminLogResponse(Long id, String username, String module, String action,
                            String ipAddress, LocalDateTime timestamp) {
        this.id = id;
        this.adminUsername = username;
        this.actionModule = module;
        this.actionMessage = action;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getAdminUsername() { return adminUsername; }
    public String getActionModule() { return actionModule; }
    public String getActionMessage() { return actionMessage; }
    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
