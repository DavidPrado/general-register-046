package com.general_registration.dto;

import jakarta.validation.constraints.NotBlank;

public record CargoEmpresaRequestDTO(
         @NotBlank(message = "O campo 'position' é obrigatório.")
         String position,
         @NotBlank(message = "O campo 'position' é obrigatório.")
         String description,
         Boolean active) {
}
