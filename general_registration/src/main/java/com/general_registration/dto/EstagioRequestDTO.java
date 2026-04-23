package com.general_registration.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record EstagioRequestDTO(
        UUID id,
        UUID idStudent,
        UUID idEnterprise,
        UUID idEmployeeSupervisor,
        LocalDate startDate,
        LocalDate endDate,
        Double intershipSalary,
        Integer hoursPerWeek,
        Integer hoursPerDay,
        Boolean terminationContract,
        String terminationContractReason
) {
}
