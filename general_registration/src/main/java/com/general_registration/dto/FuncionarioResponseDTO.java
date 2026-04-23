package com.general_registration.dto;

import java.util.UUID;

public record FuncionarioResponseDTO(
        java.util.UUID id,
        UUID idPerson,
        UUID idEnterprise,
        UUID idPositionEnterprise,
        String namePerson,
        String cpfPerson
        ) {
}
