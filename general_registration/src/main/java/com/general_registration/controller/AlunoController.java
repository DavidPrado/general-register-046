package com.general_registration.controller;


import com.general_registration.dto.AlunoRequestDTO;
import com.general_registration.dto.AlunoResponseDTO;
import com.general_registration.model.Aluno;
import com.general_registration.service.AlunoService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/alunos")
@RequiredArgsConstructor
@Slf4j
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ALUNO_CREATE')")
    public Mono<Aluno> createAluno(@Valid @RequestBody AlunoRequestDTO requestDTO) {
        return alunoService.createAluno(requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ALUNO_UPDATE')")
    public Mono<ResponseEntity<Aluno>> updateAluno(@Valid @RequestBody AlunoRequestDTO requestDTO, @PathVariable("id") UUID id) {
        return alunoService.updateAluno(requestDTO, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("Aluno não encontrado")));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ALUNO_READ')")
    public Mono<ResponseEntity<AlunoResponseDTO>> getAlunoById(@PathVariable UUID id) {
        return alunoService.getAlunoById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("Aluno não encontrado")));
    }

    @GetMapping("/pessoa/{idPerson}")
    @PreAuthorize("hasAuthority('ALUNO_READ')")
    public Mono<ResponseEntity<AlunoResponseDTO>> getAlunoByIdPerson(@PathVariable UUID idPerson) {
        return alunoService.getAlunoByIdPerson(idPerson)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ALUNO_READ')")
    public Flux<AlunoResponseDTO> getAlunoByFilter(AlunoRequestDTO filter,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return alunoService.findAllByAlunoFilter(filter, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ALUNO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteAluno(@PathVariable UUID id) {
        return alunoService.deleteAluno(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume( e -> Mono.error(new RuntimeException("Aluno não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ALUNO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAlunosBatch(@RequestBody List<UUID> ids) {
        return alunoService.deleteAlunoBatch(ids);
    }

}
