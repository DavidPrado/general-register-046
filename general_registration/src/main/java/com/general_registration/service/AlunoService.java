package com.general_registration.service;

import com.general_registration.dto.AlunoRequestDTO;
import com.general_registration.dto.AlunoResponseDTO;
import com.general_registration.matchers.AlunoMatchers;
import com.general_registration.model.Aluno;
import com.general_registration.repository.AlunoRepository;
import com.general_registration.repository.PessoaRepository;
import com.general_registration.repository.CursoAlunoRepository;
import com.general_registration.repository.ResponsavelAlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoAlunoRepository cursoAlunoRepository;
    private final ResponsavelAlunoRepository responsavelAlunoRepository;
    private final PessoaRepository personRepository;

    @Autowired
    @Lazy
    private AlunoService self;


    @Transactional
    public Mono<Aluno> createAluno(AlunoRequestDTO requestDTO) {
        return alunoRepository.existsByIdPersonAndIdSchool(requestDTO.idPerson(), requestDTO.idSchool())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new RuntimeException("Aluno já existe para a pessoa e escola fornecidas"));
                    }
                    return alunoRepository.save(dtoToAluno(requestDTO));
                });
    }


    public Mono<Boolean> existsByIdPersonAndIdSchool(UUID idPerson, UUID idSchool) {
        return alunoRepository.existsByIdPersonAndIdSchool(idPerson, idSchool);
    }

    private Aluno dtoToAluno(AlunoRequestDTO requestDTO) {
        return Aluno.builder()
                .idSchool(requestDTO.idSchool())
                .idPerson(requestDTO.idPerson())
                .build();
    }

    public Flux<AlunoResponseDTO> findAllByAlunoFilter(AlunoRequestDTO requestDTO, Pageable pageable) {
        if (requestDTO.query() != null && !requestDTO.query().isBlank()) {
            return alunoRepository.findByPersonNameOrCpf(
                    requestDTO.query(),
                    requestDTO.idSchool(),
                    pageable
            );
        }

        Aluno aluno = dtoToAluno(requestDTO);
        ExampleMatcher matcher = AlunoMatchers.listAlunoFilter();
        Example<Aluno> example = Example.of(aluno, matcher);

        return alunoRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .flatMap(this::enrichWithPersonData);
    }

    private Mono<AlunoResponseDTO> enrichWithPersonData(Aluno aluno) {
        return personRepository.findById(aluno.getIdPerson())
                .map(p -> new AlunoResponseDTO(
                        aluno.getId(),
                        aluno.getIdPerson(),
                        aluno.getIdSchool(),
                        p.getName(),
                        p.getCpf()
                ));
    }

    private AlunoResponseDTO toAlunoResponseDTO(Aluno aluno) {
        return new AlunoResponseDTO(aluno.getId(),
                aluno.getIdPerson(),
                aluno.getIdSchool(),
                null,
                null);
    }

    @Transactional
    public Mono<Aluno> updateAluno(AlunoRequestDTO requestDTO, UUID id) {
        return alunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Aluno não encontrado")))
                .flatMap(existingAluno -> {
                    existingAluno.setIdPerson(requestDTO.idPerson());
                    existingAluno.setIdSchool(requestDTO.idSchool());
                    return alunoRepository.save(existingAluno);
                });
    }


    public Mono<AlunoResponseDTO> getAlunoById(UUID id) {
        return alunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Aluno não encontrado")))
                .map(this::toAlunoResponseDTO);
    }

    public Mono<AlunoResponseDTO> getAlunoByIdPerson(UUID idPerson) {
        return alunoRepository.findByIdPerson(idPerson)
                .map(this::toAlunoResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteAluno(UUID id) {
        return alunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Aluno não encontrado")))
                .flatMap( aluno ->
                            cursoAlunoRepository.deleteByIdStudent(aluno.getId())
                            .then(responsavelAlunoRepository.deleteByIdStudent(aluno.getId()))
                            .then(alunoRepository.delete(aluno))
                        );
    }

    @Transactional
    public Mono<Void> deleteAlunoBatch(List<UUID> ids) {
        return alunoRepository.findAllById(ids)
                .collectList()
                .flatMap( alunos -> {
                    if (alunos.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Nem todos os alunos foram encontrados para os IDs fornecidos"));
                    }
                    return alunoRepository.deleteAll(alunos);
                });
    }


    @Transactional
    public Mono<Aluno> saveAndUpdate(AlunoRequestDTO requestDTO) {
        if (requestDTO == null) return Mono.empty();

        return alunoRepository.findByIdPersonAndIdSchool(requestDTO.idPerson(), requestDTO.idSchool())
                .flatMap(existingAluno -> self.updateAluno(requestDTO, existingAluno.getId())
                            .then(alunoRepository.findById(existingAluno.getId()))
                )
                .switchIfEmpty(Mono.defer(() -> alunoRepository.save(dtoToAluno(requestDTO))
                ));
    }

}
