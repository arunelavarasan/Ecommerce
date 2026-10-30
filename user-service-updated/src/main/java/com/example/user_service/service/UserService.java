package com.example.user_service.service;

import com.example.user_service.dto.request.RegisterUserRequest;
import com.example.user_service.dto.response.UserResponse;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponse register(RegisterUserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserProfile(Long id);
    Map<String, String> blockUser(Long id, String reason);
    Map<String, String> unblockUser(Long id);
    UserResponse login(String email, String password);
    Map<String, String> resetPassword(String email, String newPassword);
}
