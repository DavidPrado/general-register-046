package com.general_registration.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AlojamentoAlunoRequestDTO(
        UUID id,
        UUID idAccommodation,
        UUID idStudent,
        LocalDate entryDate,
        LocalDate exitDate
) {
}
