package com.general_registration.dto;

import jakarta.validation.constraints.NotBlank;

public record EscolaRequestDTO(
        @NotBlank(message = "O campo 'code' é obrigatório.")
        String code,
        @NotBlank(message = "O campo 'name' é obrigatório.")
        String name,
        String email,
        String phone,
        String uf,
        String city,
        String neighborhood,
        String street,
        String number,
        String complement) {
}
