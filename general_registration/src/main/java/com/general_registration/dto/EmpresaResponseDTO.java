package com.general_registration.dto;

public record EmpresaResponseDTO(
        java.util.UUID id,
        String name,
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
        String corporateName,
        String fantasyName,
        String cnae
) {
}
