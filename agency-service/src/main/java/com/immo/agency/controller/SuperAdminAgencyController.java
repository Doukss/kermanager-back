package com.immo.agency.controller;

import com.immo.agency.dto.*;
import com.immo.agency.entity.PlatformNotification;
import com.immo.agency.service.SuperAdminAgencyService;
import com.immo.common.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminAgencyController {
    private final SuperAdminAgencyService service;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> dashboard(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.dashboard()));
    }

    @GetMapping("/agencies")
    public ResponseEntity<ApiResponse<List<AgencyResponse>>> agencies(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.listAgencies()));
    }

    @PostMapping("/agencies")
    public ResponseEntity<ApiResponse<AgencyResponse>> createAgency(
            @RequestHeader("X-Role") String role,
            @Valid @RequestBody AgencyRequest request) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.createAgency(request)));
    }

    @PatchMapping("/agencies/{id}/activate")
    public ResponseEntity<ApiResponse<AgencyResponse>> activateAgency(@RequestHeader("X-Role") String role, @PathVariable UUID id) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.setAgencyActive(id, true)));
    }

    @PatchMapping("/agencies/{id}/suspend")
    public ResponseEntity<ApiResponse<AgencyResponse>> suspendAgency(@RequestHeader("X-Role") String role, @PathVariable UUID id) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.setAgencyActive(id, false)));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> subscriptions(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.listSubscriptions()));
    }

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<List<PlatformNotification>>> notifications(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.listNotifications()));
    }

    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse<PlatformNotification>> readNotification(@RequestHeader("X-Role") String role, @PathVariable UUID id) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.markNotificationRead(id)));
    }

    private void requireSuperAdmin(String role) {
        if (!"SUPER_ADMIN".equals(role)) {
            throw new SecurityException("Acces reserve au super-admin");
        }
    }
}
