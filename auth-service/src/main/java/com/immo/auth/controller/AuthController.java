package com.immo.auth.controller;
import com.immo.auth.dto.LoginRequest;
import com.immo.auth.dto.LoginResponse;
import com.immo.auth.service.AuthService;
import com.immo.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(req)));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.ok(authService.refresh(token)));
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> me(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(ApiResponse.ok(userId));
    }
}
