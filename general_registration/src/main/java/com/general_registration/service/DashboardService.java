package com.general_registration.service;

import com.general_registration.model.DashboardStats;
import com.general_registration.repository.DashboardRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
    }

    public Flux<DashboardStats> getOperationalSummary() {
        return repository.findAll();
    }
}
