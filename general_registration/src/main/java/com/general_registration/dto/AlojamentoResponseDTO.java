package com.general_registration.dto;

import java.util.UUID;

public record AlojamentoResponseDTO(UUID id,
                                    String roomNumber,
                                    String buildingBlock,
                                    Integer maxCapacity,
                                    Double squareMeters,
                                    String genderType,
                                    String status) {
}
