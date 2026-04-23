package com.general_registration.dto;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public record MatriculaCompletaDTO(
        @Valid AlunoRequestDTO aluno,
        List<UUID> courseIds,
        List<ResponsavelVinculoDTO> responsaveis
) {
}
