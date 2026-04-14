package com.planbookai.ocr.controller;

import com.planbookai.ocr.model.DashboardStats;
import com.planbookai.ocr.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')") // Admin quản hệ thống, Manager quản kinh doanh
    public ResponseEntity<DashboardStats> getStats() {
        return ResponseEntity.ok(dashboardService.getSystemOverview());
    }
}