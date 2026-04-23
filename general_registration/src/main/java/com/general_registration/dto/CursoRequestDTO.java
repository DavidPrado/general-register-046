package com.general_registration.dto;

import jakarta.validation.constraints.NotBlank;

public record CursoRequestDTO(
        @NotBlank(message = "O campo 'name' é obrigatório.")
        String name,
        String description,
        Boolean active
) {


}
