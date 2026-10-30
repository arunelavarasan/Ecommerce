package com.example.admin_service.dto.request;

public class CreateAdminLogRequest {
    private String username;
    private String module = "GENERAL";
    private String action;
    private String ipAddress = "127.0.0.1";

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
