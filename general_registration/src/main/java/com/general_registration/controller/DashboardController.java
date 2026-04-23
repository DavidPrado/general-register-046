package com.general_registration.controller;

import com.general_registration.model.DashboardStats;
import com.general_registration.repository.DashboardRepository;
import com.general_registration.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public Flux<DashboardStats> getSummary() {
        return dashboardService.getOperationalSummary();
    }
}
