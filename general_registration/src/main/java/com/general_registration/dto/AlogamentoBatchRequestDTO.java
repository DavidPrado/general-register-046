package com.general_registration.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AlogamentoBatchRequestDTO(
        String prefixRoom,
        @Positive(message = "O número inicial deve ser maior que zero")
        Integer initialNumberRoom,
        @Positive(message = "O número final deve ser maior que zero")
        Integer finalNumberRoom,
        @NotBlank(message = "O bloco é obrigatório")
        String buildingBlock,
        @Min(value = 1, message = "A capacidade mínima é 1")
        Integer maxCapacity,
        @DecimalMin(value = "1.0", message = "A metragem mínima é 1m²")
        Double squareMeters,
        @NotBlank(message = "O gênero deve ser definido")
        String genderType,
        @NotBlank(message = "O status inicial é obrigatório")
        String status) {
}
