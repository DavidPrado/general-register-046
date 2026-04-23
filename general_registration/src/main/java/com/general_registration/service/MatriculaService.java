package com.general_registration.service;

import com.general_registration.dto.MatriculaCompletaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final AlunoService alunoService;
    private final CursoAlunoService cursoAlunoService;
    private final ResponsavelAlunoService responsavelAlunoService;

    @Transactional
    public Mono<Void> salvarMatriculaIntegrada(MatriculaCompletaDTO requestDTO){
        return alunoService.saveAndUpdate(requestDTO.aluno())
                .flatMap( aluno -> {
                    Mono<Void> cursosMono = cursoAlunoService.saveBatch(aluno.getId(), requestDTO.courseIds());
                    Mono<Void> responsaveisMono = responsavelAlunoService.saveBatch(aluno.getId(), requestDTO.responsaveis());

                    return Mono.when(cursosMono, responsaveisMono);
                });
    }

}
