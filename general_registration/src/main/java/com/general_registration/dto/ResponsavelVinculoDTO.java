package com.general_registration.dto;

import com.general_registration.enums.Parentesco;

import java.util.UUID;

public record ResponsavelVinculoDTO(UUID id,
                                    UUID idPerson,
                                    Parentesco kinship) {

}
