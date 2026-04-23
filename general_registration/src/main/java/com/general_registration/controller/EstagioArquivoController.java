package com.general_registration.controller;

import com.general_registration.dto.UploadResponseDTO;
import com.general_registration.service.EstagioArquivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/estagios-arquivos")
public class EstagioArquivoController {

    private final EstagioArquivoService estagioArquivoService;

    public EstagioArquivoController(EstagioArquivoService estagioArquivoService) {
        this.estagioArquivoService = estagioArquivoService;
    }

    @PostMapping(value = "/{idInternship}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_UPDATE') or hasAuthority('ESTAGIO_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<List<UploadResponseDTO>> uploadFiles(@PathVariable UUID idInternship,
                                                     @RequestPart("description") String description,
                                                     @RequestPart("files") Flux<FilePart> files) {

        return files.flatMap(filePart -> estagioArquivoService.uploadFile(idInternship, description, filePart), 2)
                .collectList();
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_READ')")
    public Mono<ResponseEntity<byte[]>> downloadFile(@PathVariable UUID id) {
        return estagioArquivoService.downloadFile(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteFile(@PathVariable UUID id) {
        return estagioArquivoService.deleteFileComplete(id);
    }

}
