package com.general_registration.controller;

import com.general_registration.dto.EstagioDescArquivoRequestDTO;
import com.general_registration.dto.EstagioDescArquivoResponseDTO;
import com.general_registration.service.EstagioDescArquivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/estagios-desc-arquivos")
@RequiredArgsConstructor
@Slf4j
public class EstagioDescArquivoController {

    private final EstagioDescArquivoService estagioArquivoService;


    @PostMapping
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_CREATE')")
    public Mono<EstagioDescArquivoResponseDTO> createEstagioArquivo(@Valid @RequestBody EstagioDescArquivoRequestDTO requestDTO) {
        return estagioArquivoService.createEstagioArquivo(requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_UPDATE')")
    public Mono<ResponseEntity<EstagioDescArquivoResponseDTO>> updateEstagioArquivo(@PathVariable UUID id, @Valid @RequestBody EstagioDescArquivoRequestDTO requestDTO) {
        return estagioArquivoService.updateEstagioArquivo(id, requestDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estágio Arquivo não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_READ')")
    public Mono<Page<EstagioDescArquivoResponseDTO>> getEstagioArquivoById(EstagioDescArquivoRequestDTO filter,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                           @RequestParam(defaultValue = "DESC") String direction){
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return estagioArquivoService.findAllByEstagioArquivoFilter(filter, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEstagioArquivo(@PathVariable UUID id) {
        return estagioArquivoService.deleteEstagioArquivo(id);
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEstagioArquivosBatch(@RequestBody List<UUID> ids) {
        return estagioArquivoService.deleteEstagioArquivoBatch(ids);
    }

    @GetMapping("/estagio/{idInternship}")
    @PreAuthorize("hasAuthority('ESTAGIO_ARQUIVO_READ')")
    public Mono<ResponseEntity<List<EstagioDescArquivoResponseDTO>>> getFilesByEstagioId(@PathVariable UUID idInternship) {
        return estagioArquivoService.findAllByInternshipId(idInternship)
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
    }
}
