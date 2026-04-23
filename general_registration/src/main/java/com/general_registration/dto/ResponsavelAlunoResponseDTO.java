package com.general_registration.dto;

import com.general_registration.enums.Parentesco;

public record ResponsavelAlunoResponseDTO (
        java.util.UUID id,
        java.util.UUID idStudent,
        java.util.UUID idPerson,
        Parentesco kinship) {
}
