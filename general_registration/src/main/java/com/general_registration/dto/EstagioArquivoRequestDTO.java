package com.general_registration.dto;

import java.util.UUID;

public record EstagioArquivoRequestDTO(UUID idFile,
                                       byte[] binaryContent,
                                       Integer sequenceOrder) {
}
