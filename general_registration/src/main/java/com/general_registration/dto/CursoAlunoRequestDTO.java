package com.general_registration.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CursoAlunoRequestDTO(
        @NotNull(message = "O campo 'idStudent' é obrigatório.")
        UUID idStudent,
        @NotNull(message = "O campo 'idCourse' é obrigatório.")
        UUID idCourse
) {
}
