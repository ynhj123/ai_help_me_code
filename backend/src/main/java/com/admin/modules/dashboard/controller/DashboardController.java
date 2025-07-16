package com.admin.modules.dashboard.controller;

import com.admin.modules.dashboard.dto.DashboardStatsDto;
import com.admin.modules.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/realtime")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<DashboardStatsDto> getRealtimeStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}