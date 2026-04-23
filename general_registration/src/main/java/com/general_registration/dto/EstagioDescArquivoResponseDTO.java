package com.general_registration.dto;

import java.util.UUID;

public record EstagioDescArquivoResponseDTO(
        UUID id,
        UUID idInternship,
        String fileName,
        String fileDescription,
        Integer fileSize,
        String mimeType) {
}
