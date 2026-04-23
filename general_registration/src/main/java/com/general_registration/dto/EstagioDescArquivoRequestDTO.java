package com.general_registration.dto;

import java.util.UUID;

public record EstagioDescArquivoRequestDTO(
         UUID idInternship,
         String fileName,
         String fileDescription,
         Integer fileSize
) {
}
