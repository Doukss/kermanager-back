package com.immo.auth.controller;

import com.immo.auth.dto.SuperAdminUserRequest;
import com.immo.auth.dto.SuperAdminUserResponse;
import com.immo.auth.service.SuperAdminUserService;
import com.immo.common.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super-admin/users")
@RequiredArgsConstructor
public class SuperAdminUserController {
    private final SuperAdminUserService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SuperAdminUserResponse>>> list(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.listPlatformUsers()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SuperAdminUserResponse>> create(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody SuperAdminUserRequest request) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<SuperAdminUserResponse>> activate(
            @RequestHeader("X-Role") String role,
            @PathVariable UUID id) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.setActive(id, true)));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<SuperAdminUserResponse>> suspend(
            @RequestHeader("X-Role") String role,
            @PathVariable UUID id) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.setActive(id, false)));
    }

    private void requireSuperAdmin(String role) {
        if (!"SUPER_ADMIN".equals(role)) {
            throw new SecurityException("Acces reserve au super-admin");
        }
    }
}
