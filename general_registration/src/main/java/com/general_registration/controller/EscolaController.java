package com.general_registration.controller;

import com.general_registration.dto.EscolaRequestDTO;
import com.general_registration.dto.EscolaResponseDTO;
import com.general_registration.model.Escola;
import com.general_registration.service.EscolaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/escolas")
@RequiredArgsConstructor
@Slf4j
public class EscolaController {

    private final EscolaService escolaService;

    @PostMapping
    @PreAuthorize("hasAuthority('ESCOLA_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createEscola(@Valid @RequestBody EscolaRequestDTO escolaRequestDTO) {
        return escolaService.createEmpresa(escolaRequestDTO);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ESCOLA_UPDATE')")
    public Mono<ResponseEntity<Escola>> updateEscola(@PathVariable UUID id, @Valid @RequestBody EscolaRequestDTO escolaRequestDTO) {
        return escolaService.updateEscola(id, escolaRequestDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrado")));

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESCOLA_READ')")
    public Flux<EscolaResponseDTO> getEscolaFilter(EscolaRequestDTO filter,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                      @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return escolaService.findAllByEscolaFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ESCOLA_READ')")
    public Mono<ResponseEntity<EscolaResponseDTO>> getEscolaById(@PathVariable UUID id) {
        return escolaService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ESCOLA_DELETE')")
    public Mono<ResponseEntity<Object>> deleteEscola(@PathVariable UUID id) {
        return escolaService.deleteEscola(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ESCOLA_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEscolasBatch(@RequestBody List<UUID> ids) {
        return escolaService.deleteEscolaBatch(ids);
    }
}
