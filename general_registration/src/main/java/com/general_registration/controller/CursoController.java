package com.general_registration.controller;


import com.general_registration.dto.CursoRequestDTO;
import com.general_registration.dto.CursoResponseDTO;
import com.general_registration.model.Curso;
import com.general_registration.service.CursoService;
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
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Slf4j
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    @PreAuthorize("hasAuthority('CURSO_CREATE')")
    public Mono<Void> createCurso(@Valid @RequestBody CursoRequestDTO requestDTO) {
        return cursoService.createCurso(requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CURSO_UPDATE')")
    public Mono<ResponseEntity<Curso>> updateCurso(@Valid @RequestBody CursoRequestDTO requestDTO, @PathVariable("id") UUID id) {
        return cursoService.updateCurso(requestDTO, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CURSO_READ')")
    public Mono<Page<CursoResponseDTO>> getCursosByFilter(CursoRequestDTO requestDTO,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return cursoService.findByCursoFilter(requestDTO , pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CURSO_READ')")
    public Mono<ResponseEntity<CursoResponseDTO>> getCursoById(@PathVariable("id") UUID id) {
        return cursoService.getCursoById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CURSO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteCurso(@PathVariable("id") UUID id) {
        return cursoService.deleteCurso(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume( e ->Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('CURSO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCursosBatch(@RequestBody List<UUID> ids) {
        return cursoService.deleteCursosBatch(ids);
    }
}
