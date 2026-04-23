package com.general_registration.repository;

import com.general_registration.model.DashboardStats;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface DashboardRepository extends R2dbcRepository<DashboardStats, UUID> {

    Flux<DashboardStats> findByCategory(String category);
}
