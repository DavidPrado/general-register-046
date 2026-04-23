package com.general_registration.dto;

import java.util.UUID;

public record EstagioArquivoResponseDTO(UUID id,
                                        UUID idFile,
                                        byte[] binaryContent,
                                        Integer sequenceOrder) {
}
