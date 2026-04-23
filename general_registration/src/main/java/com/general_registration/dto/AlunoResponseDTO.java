package com.general_registration.dto;

import java.util.UUID;

public record AlunoResponseDTO(
         UUID id,
         UUID idPerson,
         UUID idSchool,
         String nomePessoa,
         String cpfPessoa
) {
}
