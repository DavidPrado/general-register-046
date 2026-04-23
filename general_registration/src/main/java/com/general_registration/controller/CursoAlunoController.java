package com.general_registration.controller;


import com.general_registration.dto.CursoAlunoRequestDTO;
import com.general_registration.dto.CursoAlunoResponseDTO;
import com.general_registration.model.CursoAluno;
import com.general_registration.service.CursoAlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cursoalunos")
@RequiredArgsConstructor
@Slf4j
public class CursoAlunoController {

    private final CursoAlunoService cursoAlunoService;

    @PostMapping
    @PreAuthorize("hasAuthority('CURSO_ALUNO_CREATE')")
    public Mono<Void> createCursoAluno(@Valid @RequestBody CursoAlunoRequestDTO cursoAlunoRequestDTO) {
        return cursoAlunoService.createCursoAluno(cursoAlunoRequestDTO);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('CURSO_ALUNO_UPDATE')")
    public Mono<ResponseEntity<CursoAluno>> updateCursoAluno(@Valid @RequestBody CursoAlunoRequestDTO cursoAlunoRequestDTO, @PathVariable("id") UUID id) {
        return cursoAlunoService.updateCursoAluno(cursoAlunoRequestDTO, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("CursoAluno não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CURSO_ALUNO_READ')")
    public Flux<CursoAlunoResponseDTO> getCursoAlunoFilter(CursoAlunoRequestDTO filter,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "createdAt") String sortBy,
                                                           @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return cursoAlunoService.findAllByCursoAlunoFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CURSO_ALUNO_READ')")
    public Mono<ResponseEntity<CursoAlunoResponseDTO>> getCursoAlunoById(@PathVariable UUID id) {
        return cursoAlunoService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("CursoAluno não encontrado")));

    }

    @GetMapping("/aluno/{idStudent}")
    @PreAuthorize("hasAuthority('CURSO_ALUNO_READ')")
    public Flux<CursoAlunoResponseDTO> getCursoAlunoByIdStudent(@PathVariable UUID idStudent) {
        return cursoAlunoService.findByIdStudent(idStudent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CURSO_ALUNO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteCursoAluno(@PathVariable UUID id) {
        return cursoAlunoService.deleteCursoAluno(id)
                .thenReturn(ResponseEntity.ok().build())
                .switchIfEmpty(Mono.error(new RuntimeException("CursoAluno não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('CURSO_ALUNO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteCursoAlunosBatch(@RequestBody List<UUID> ids) {
        return cursoAlunoService.deleteCursoAlunoBatch(ids)
                .thenReturn(ResponseEntity.ok().build())
                .switchIfEmpty(Mono.error(new RuntimeException("CursoAlunos não encontrados")));
    }



}
