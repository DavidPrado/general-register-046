package com.general_registration.dto;

public record EscolaResponseDTO (
        java.util.UUID id,
        String code,
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
