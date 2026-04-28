package com.general_registration.controller;

import com.general_registration.dto.AlojamentoAlunoRequestDTO;
import com.general_registration.dto.AlojamentoAlunoResponseDTO;
import com.general_registration.model.AlojamentoAluno;
import com.general_registration.service.AlojamentoAlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/alojamento-alunos")
@RequiredArgsConstructor
@Slf4j
public class AlojamentoAlunoController {

    private final AlojamentoAlunoService alojamentoAlunoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ALOJAMENTO_ALUNO_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AlojamentoAluno> createAlojamentoAluno(@Valid @RequestBody AlojamentoAlunoRequestDTO request) {
        return alojamentoAlunoService.createAlojamentoAluno(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ALOJAMENTO_ALUNO_UPDATE')")
    public Mono<ResponseEntity<AlojamentoAluno>> updateAlojamentoAluno(@PathVariable("id") UUID id, @Valid @RequestBody AlojamentoAlunoRequestDTO request) {
        return alojamentoAlunoService.updateAlojamentoAluno(id, request)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento do Aluno não encontrado")));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ALOJAMENTO_ALUNO_READ')")
    public Mono<ResponseEntity<AlojamentoAlunoResponseDTO>> getAlojamentoAlunoById(@PathVariable UUID id) {
        return alojamentoAlunoService.findAlojamentoAlunoById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento do Aluno não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ALOJAMENTO_ALUNO_DELETE')")
    public Mono<Void> deleteAlojamentoAluno(@PathVariable UUID id) {
        return alojamentoAlunoService.deleteAlojamentoAluno(id);
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ALOJAMENTO_ALUNO_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAlojamentoAlunoBatch(@RequestBody List<UUID> ids){
        return alojamentoAlunoService.deleteAlojamentoAlunoBatch(ids);
    }
}
