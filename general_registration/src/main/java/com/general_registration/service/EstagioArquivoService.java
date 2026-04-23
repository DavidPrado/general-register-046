package com.general_registration.service;

import com.general_registration.dto.UploadResponseDTO;
import com.general_registration.model.EstagioArquivo;
import com.general_registration.model.EstagioDescArquivo;
import com.general_registration.repository.EstagioArquivoRepository;
import com.general_registration.repository.EstagioDescArquivoRepository;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
public class EstagioArquivoService {

    private final EstagioDescArquivoRepository estagioDescArquivoRepository;
    private final EstagioArquivoRepository estagioArquivoRepository;

    public EstagioArquivoService(EstagioDescArquivoRepository estagioDescArquivoRepository, EstagioArquivoRepository estagioArquivoRepository) {
        this.estagioDescArquivoRepository = estagioDescArquivoRepository;
        this.estagioArquivoRepository = estagioArquivoRepository;
    }

    @Transactional
    public Mono<UploadResponseDTO> uploadFile(UUID internshipId, String description, FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    // 1. Criamos o Metadado primeiro
                    EstagioDescArquivo meta = EstagioDescArquivo.builder()
                            .idInternship(internshipId)
                            .fileName(filePart.filename())
                            .fileSize(bytes.length)
                            .fileDescription(description)
                            .mimeType(filePart.headers().getContentType() != null ?
                                    filePart.headers().getContentType().toString() : "application/octet-stream")
                            .build();

                    return estagioDescArquivoRepository.save(meta)
                            .flatMap(savedMeta -> {
                                // 2. Salvamos o binário usando o ID gerado pelo metadado
                                EstagioArquivo arquivo = EstagioArquivo.builder()
                                        .idFile(savedMeta.getId())
                                        .binaryContent(bytes)
                                        .build();

                                return estagioArquivoRepository.save(arquivo)
                                        .map(saved -> new UploadResponseDTO(
                                                savedMeta.getFileName(),
                                                "Upload realizado com sucesso!"
                                        ));
                            });
                });
    }

    @Transactional(readOnly = true)
    public Mono<ResponseEntity<byte[]>> downloadFile(UUID fileId) {
        return estagioDescArquivoRepository.findById(fileId)
                .zipWith(estagioArquivoRepository.findById(fileId)) // Buscamos o binário pelo mesmo ID
                .map(tuple -> {
                    EstagioDescArquivo meta = tuple.getT1();
                    EstagioArquivo binary = tuple.getT2();

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + meta.getFileName() + "\"")
                            .contentType(MediaType.parseMediaType(meta.getMimeType()))
                            .body(binary.getBinaryContent());
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado")));
    }

    @Transactional
    public Mono<Void> deleteFileComplete(UUID id) {
        return estagioDescArquivoRepository.deleteById(id);
    }


}
