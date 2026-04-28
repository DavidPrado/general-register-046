package com.general_registration.controller;


import com.general_registration.dto.AlogamentoBatchRequestDTO;
import com.general_registration.dto.AlojamentoRequestDTO;
import com.general_registration.dto.AlojamentoResponseDTO;
import com.general_registration.model.Alojamento;
import com.general_registration.service.AlojamentoService;
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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/alojamentos")
@RequiredArgsConstructor
@Slf4j
public class AlojamentoController {

    private final AlojamentoService alojamentoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ALOJAMENTO_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Alojamento> createAlojamento(@Valid @RequestBody AlojamentoRequestDTO requestDTO) {
        return alojamentoService.createAlojamento(requestDTO);
    }

    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('ALOJAMENTO_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createAlojamentoBatch(@Valid @RequestBody AlogamentoBatchRequestDTO requestDTO) {
        return alojamentoService.createAlojamentoBatch(requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ALOJAMENTO_UPDATE')")
    public Mono<ResponseEntity<Alojamento>> updateAlojamento(@PathVariable UUID id, @Valid @RequestBody AlojamentoRequestDTO requestDTO) {
        return alojamentoService.updateAlojamento(id, requestDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ALOJAMENTO_READ')")
    public Mono<Page<AlojamentoResponseDTO>> getAlojamentoFilter(AlojamentoRequestDTO filter,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                 @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return alojamentoService.findAllByAlojamentoFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ALOJAMENTO_READ')")
    public Mono<ResponseEntity<AlojamentoResponseDTO>> getAlojamentoById(@PathVariable UUID id) {
        return alojamentoService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ALOJAMENTO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAlojamento(@PathVariable UUID id) {
        return alojamentoService.deleteAlojamento(id);
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ALOJAMENTO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAlojamentos(@RequestBody List<UUID> ids) {
        return alojamentoService.deleteAlogamentoBatch(ids);
    }
}
