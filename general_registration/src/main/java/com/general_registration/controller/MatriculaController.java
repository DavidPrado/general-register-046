package com.general_registration.controller;

import com.general_registration.dto.MatriculaCompletaDTO;
import com.general_registration.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/matriculas")
@RequiredArgsConstructor
@Slf4j
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    @PreAuthorize("hasAuthority('MATRICULA_CREATE')")
    public Mono<Void> salvarMatricula(@Valid @RequestBody MatriculaCompletaDTO matriculaRequestDTO) {
        return matriculaService.salvarMatriculaIntegrada(matriculaRequestDTO);
    }

}
