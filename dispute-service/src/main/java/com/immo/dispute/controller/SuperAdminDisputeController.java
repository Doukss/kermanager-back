package com.immo.dispute.controller;

import com.immo.common.dto.ApiResponse;
import com.immo.dispute.dto.DisputeStatsResponse;
import com.immo.dispute.dto.SuperAdminDisputeResponse;
import com.immo.dispute.service.SuperAdminDisputeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin/disputes")
@RequiredArgsConstructor
public class SuperAdminDisputeController {
    private final SuperAdminDisputeService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SuperAdminDisputeResponse>>> list(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.listDisputes()));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DisputeStatsResponse>> stats(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.stats()));
    }

    private void requireSuperAdmin(String role) {
        if (!"SUPER_ADMIN".equals(role)) {
            throw new SecurityException("Acces reserve au super-admin");
        }
    }
}
