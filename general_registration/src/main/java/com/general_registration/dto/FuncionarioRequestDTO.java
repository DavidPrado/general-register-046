package com.general_registration.dto;

import java.util.UUID;

public record FuncionarioRequestDTO(
        UUID idPerson,
        UUID idEnterprise,
        UUID idPositionEnterprise,
        String query
) {
}
