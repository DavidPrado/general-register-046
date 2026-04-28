package com.general_registration.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AlojamentoAlunoViewRequestDTO(
        UUID id,
        String roomNumber,
        String buildingBlock,
        Integer maxCapacity,
        String genderType,
        String studentName,
        LocalDate entryDate,
        LocalDate exitDate,
        String statusAccommodation
) {
}
