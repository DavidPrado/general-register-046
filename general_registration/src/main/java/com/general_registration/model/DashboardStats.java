package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("dashboard_stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStats {

    @Id
    UUID id;
    String metricName;
    Integer metricValue;
    String category;
    LocalDateTime updatedAt;
}
