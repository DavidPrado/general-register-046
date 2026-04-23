package com.general_registration.controller;

import com.general_registration.dto.EmpresaRequestDTO;
import com.general_registration.dto.EmpresaResponseDTO;
import com.general_registration.model.Empresa;
import com.general_registration.service.EmpresaService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/empresas")
@RequiredArgsConstructor
@Slf4j
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPRESA_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createEmpresa(@Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        return empresaService.createEmpresa(empresaRequestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPRESA_UPDATE')")
    public Mono<ResponseEntity<Empresa>> updateEmpresa(@PathVariable UUID id, @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        return empresaService.updateEmpresa(id, empresaRequestDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPRESA_READ')")
    public Mono<Page<EmpresaResponseDTO>> getEmpresaFilter(EmpresaRequestDTO filter,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "createdAt") String sortBy,
                                                           @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return empresaService.findAllByEmpresaFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPRESA_READ')")
    public Mono<ResponseEntity<EmpresaResponseDTO>> getEmpresaById(@PathVariable UUID id) {
        return empresaService.getEmpresaById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPRESA_DELETE')")
    public Mono<ResponseEntity<Object>> deleteEmpresa(@PathVariable UUID id) {
        return empresaService.deleteEmpresa(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('EMPRESA_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmpresasBatch(@RequestBody List<UUID> ids) {
        return empresaService.deleteEmpresaBatch(ids);
    }
}

