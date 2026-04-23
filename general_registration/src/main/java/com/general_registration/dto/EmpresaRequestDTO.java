package com.general_registration.dto;

import jakarta.validation.constraints.NotBlank;

public record EmpresaRequestDTO(
        @NotBlank(message = "O campo 'name' é obrigatório.")
        String name,
        @NotBlank(message = "O campo 'cnpj' é obrigatório.")
        String cnpj,
        String email,
        String phone,
        String uf,
        String city,
        String neighborhood,
        String street,
        String cep,
        String number,
        String complement,
        String stateRegistration,
        String municipalRegistration,
        @NotBlank(message = "O campo 'corporateName' é obrigatório.")
        String corporateName,
        @NotBlank(message = "O campo 'corporateName' é obrigatório.")
        String fantasyName,
        String cnae) {
}
