package com.general_registration.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AlunoRequestDTO(@NotNull(message = "A pessoa é obrigatória")
                              UUID idPerson,
                              @NotNull(message = "A escola é obrigatória")
                              UUID idSchool,
                              String query
) {
}
