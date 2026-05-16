package com.immo.payment.controller;

import com.immo.common.dto.ApiResponse;
import com.immo.payment.dto.PaymentStatsResponse;
import com.immo.payment.dto.SuperAdminPaymentResponse;
import com.immo.payment.service.SuperAdminPaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin/payments")
@RequiredArgsConstructor
public class SuperAdminPaymentController {
    private final SuperAdminPaymentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SuperAdminPaymentResponse>>> list(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.listPayments()));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<PaymentStatsResponse>> stats(@RequestHeader("X-Role") String role) {
        requireSuperAdmin(role);
        return ResponseEntity.ok(ApiResponse.ok(service.stats()));
    }

    private void requireSuperAdmin(String role) {
        if (!"SUPER_ADMIN".equals(role)) {
            throw new SecurityException("Acces reserve au super-admin");
        }
    }
}
