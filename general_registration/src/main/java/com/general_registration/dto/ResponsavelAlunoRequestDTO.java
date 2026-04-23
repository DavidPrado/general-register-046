package com.general_registration.dto;

import com.general_registration.enums.Parentesco;

public record ResponsavelAlunoRequestDTO(
            java.util.UUID idStudent,
            java.util.UUID idPerson,
            Parentesco kinship
) {
}
