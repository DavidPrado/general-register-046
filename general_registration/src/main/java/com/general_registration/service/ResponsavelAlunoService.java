package com.general_registration.service;

import com.general_registration.dto.ResponsavelAlunoRequestDTO;
import com.general_registration.dto.ResponsavelAlunoResponseDTO;
import com.general_registration.dto.ResponsavelVinculoDTO;
import com.general_registration.matchers.ResponsavelAlunoMatchers;
import com.general_registration.model.ResponsavelAluno;
import com.general_registration.repository.ResponsavelAlunoRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ResponsavelAlunoService {

    private final ResponsavelAlunoRepository responsavelAlunoRepository;

    @Transactional
    public Mono<Void> createResponsavelAluno(ResponsavelAlunoRequestDTO requestDTO) {
        return responsavelAlunoRepository.save(dtoToResponsavelAluno(requestDTO)).then();
    }

    private ResponsavelAluno dtoToResponsavelAluno(ResponsavelAlunoRequestDTO requestDTO) {
        return ResponsavelAluno.builder()
                .idStudent(requestDTO.idStudent())
                .idPerson(requestDTO.idPerson())
                .kinship(requestDTO.kinship())
                .build();
    }

    public Flux<ResponsavelAlunoResponseDTO> findAllByResponsavelAlunoFilter(ResponsavelAlunoRequestDTO requestDTO, Pageable pageable) {
        ResponsavelAluno responsavelAluno = dtoToResponsavelAluno(requestDTO);
        ExampleMatcher matcher = ResponsavelAlunoMatchers.listResponsavelAlunoFilter();
        Example<ResponsavelAluno> example = Example.of(responsavelAluno, matcher);

        return responsavelAlunoRepository.findAll(example , pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toResponsavelAlunoResponseDTO);
    }

    private ResponsavelAlunoResponseDTO toResponsavelAlunoResponseDTO(ResponsavelAluno responsavelAluno) {
        return new ResponsavelAlunoResponseDTO(responsavelAluno.getId(),
                responsavelAluno.getIdStudent(),
                responsavelAluno.getIdPerson(),
                responsavelAluno.getKinship());
    }

    public Mono<ResponsavelAlunoResponseDTO> findById(UUID id) {
        return responsavelAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Responsável do Aluno não encontrado")))
                .map(this::toResponsavelAlunoResponseDTO);
    }

    public Flux<ResponsavelAlunoResponseDTO> findByIdStudent(UUID idStudent) {
        return responsavelAlunoRepository.findByIdStudent(idStudent)
                .switchIfEmpty(Mono.error(new RuntimeException("Responsável do Aluno não encontrado")))
                .map(this::toResponsavelAlunoResponseDTO);
    }

    @Transactional
    public Mono<ResponsavelAluno> updateResponsavelAluno(ResponsavelAlunoRequestDTO requestDTO, UUID id) {
        return responsavelAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Responsável do Aluno não encontrado")))
                .flatMap(existingResponsavelAluno -> {
                    existingResponsavelAluno.setIdStudent(requestDTO.idStudent());
                    existingResponsavelAluno.setIdPerson(requestDTO.idPerson());
                    existingResponsavelAluno.setKinship(requestDTO.kinship());
                    return responsavelAlunoRepository.save(existingResponsavelAluno);
                });
    }

    @Transactional
    public Mono<Void> deleteResponsavelAluno(UUID id) {
        return responsavelAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Responsável do Aluno não encontrado")))
                .flatMap(responsavelAlunoRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteResponsavelAlunoBatch(List<UUID> ids) {
        return null;
    }


   @Transactional
   public Mono<Void> saveBatch(UUID studentId, List<ResponsavelVinculoDTO> dtos) {
       if (dtos == null || dtos.isEmpty()) return Mono.empty();

       var novos = dtos.stream()
               .filter(d -> d.id() == null)
               .map(d -> ResponsavelAluno.builder()
                       .idStudent(studentId)
                       .idPerson(d.idPerson())
                       .kinship(d.kinship())
                       .build())
               .toList();

       var existentes = dtos.stream()
               .filter(d -> d.id() != null)
               .map(d -> ResponsavelAluno.builder()
                       .id(d.id())
                       .idStudent(studentId)
                       .idPerson(d.idPerson())
                       .kinship(d.kinship())
                       .build())
               .toList();

       Mono<Void> insertFlux = responsavelAlunoRepository.saveAll(novos).then();
       Mono<Void> updateFlux = responsavelAlunoRepository.saveAll(existentes).then();

       return Mono.when(insertFlux, updateFlux);
   }
}
