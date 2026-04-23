package com.general_registration.dto;

public record CursoResponseDTO(
        java.util.UUID id,
        String name,
        String description,
        Boolean active) {
}
