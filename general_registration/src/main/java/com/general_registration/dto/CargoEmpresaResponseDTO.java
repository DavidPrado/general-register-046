package com.general_registration.dto;


import java.util.UUID;

public record CargoEmpresaResponseDTO(UUID id,
                                      String position,
                                      String description,
                                      Boolean active) {
}
