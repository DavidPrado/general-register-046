package com.general_registration.dto;

import java.util.UUID;

public record EstagioVencidoDTO(
        UUID estagioId,
        String alunoNome,
        UUID idStudent
) {
}
