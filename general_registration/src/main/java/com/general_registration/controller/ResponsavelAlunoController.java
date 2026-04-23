package com.general_registration.controller;

import com.general_registration.dto.ResponsavelAlunoRequestDTO;
import com.general_registration.dto.ResponsavelAlunoResponseDTO;
import com.general_registration.model.ResponsavelAluno;
import com.general_registration.service.ResponsavelAlunoService;
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
@RequestMapping("api/responsavel-alunos")
@RequiredArgsConstructor
@Slf4j
public class ResponsavelAlunoController {

    private final ResponsavelAlunoService responsavelAlunoService;

    @PostMapping
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_CREATE')")
    public Mono<Void> createResponsavelAluno(@Valid @RequestBody ResponsavelAlunoRequestDTO responsavelAlunoRequestDTO) {
        return responsavelAlunoService.createResponsavelAluno(responsavelAlunoRequestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_UPDATE')")
    public Mono<ResponseEntity<ResponsavelAluno>> updateResponsavelAluno(@Valid @RequestBody ResponsavelAlunoRequestDTO responsavelAlunoRequestDTO, @PathVariable("id") UUID id) {
        return responsavelAlunoService.updateResponsavelAluno(responsavelAlunoRequestDTO, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável do Aluno não encontrado")));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_READ')")
    public Mono<ResponseEntity<ResponsavelAlunoResponseDTO>> getResponsavelAlunoById(@PathVariable UUID id) {
        return responsavelAlunoService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável do Aluno não encontrado")));
    }


    @GetMapping("/aluno/{idStudent}")
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_READ')")
    public Flux<ResponsavelAlunoResponseDTO> getResponsavelAlunoByIdStudent(@PathVariable UUID idStudent) {
        return responsavelAlunoService.findByIdStudent(idStudent);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_READ')")
    public Flux<ResponsavelAlunoResponseDTO> getResponsavelAlunoByFilter(ResponsavelAlunoRequestDTO filter,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "createdAt") String sortBy,
                                                            @RequestParam(defaultValue = "DESC") String direction) {

        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return responsavelAlunoService.findAllByResponsavelAlunoFilter(filter, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteResponsavelAluno(@PathVariable UUID id) {
        return responsavelAlunoService.deleteResponsavelAluno(id)
                .thenReturn(ResponseEntity.noContent().build())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável do Aluno não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('RESPONSAVEL_ALUNO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteResponsavelAlunosBatch(@RequestBody List<UUID> ids) {
        return responsavelAlunoService.deleteResponsavelAlunoBatch(ids);
    }

}
