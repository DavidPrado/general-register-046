package com.general_registration.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EstagioResponseDTO(
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
        String terminationContractReason,
        String studentName,
        String studentCpf,
        String enterpriseName,
        String enterpriseCnpj
) {
}
