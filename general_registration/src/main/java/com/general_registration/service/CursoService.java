package com.general_registration.service;

import com.general_registration.dto.CursoRequestDTO;
import com.general_registration.dto.CursoResponseDTO;
import com.general_registration.matchers.CursoMatchers;
import com.general_registration.model.Curso;
import com.general_registration.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    @Transactional
    public Mono<Void> createCurso(CursoRequestDTO dto) {
        return cursoRepository.save(dtoToCurso(dto)).then();
    }

    private Curso dtoToCurso(CursoRequestDTO requestDTO) {
        return Curso.builder()
                .name(requestDTO.name())
                .description(requestDTO.description())
                .active(requestDTO.active())
                .build();
    }

    public  Mono<Page<CursoResponseDTO>> findByCursoFilter(CursoRequestDTO requestDTO, Pageable pageable) {
        Curso curso = dtoToCurso(requestDTO);
        ExampleMatcher matcher = CursoMatchers.listCursoFilter();
        Example<Curso> example = Example.of(curso, matcher);

        Mono<List<CursoResponseDTO>> data = cursoRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toCursoResponseDTO)
                .collectList();

        Mono<Long> total = cursoRepository.count(example);

        return Mono.zip(data, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private CursoResponseDTO toCursoResponseDTO(Curso curso) {
        return new CursoResponseDTO(curso.getId(),
                curso.getName(),
                curso.getDescription(),
                curso.getActive());
    }

    @Transactional
    public Mono<Curso> updateCurso(CursoRequestDTO requestDTO, UUID id) {
        return cursoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Curso não encontrado")))
                .flatMap(existingCurso -> {
                    existingCurso.setName(requestDTO.name());
                    existingCurso.setDescription(requestDTO.description());
                    existingCurso.setActive(requestDTO.active());
                    return cursoRepository.save(existingCurso);
                });
    }

    public Mono<CursoResponseDTO> getCursoById(UUID id) {
        return cursoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Curso não encontrado")))
                .map(this::toCursoResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteCurso(UUID id) {
        return cursoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Curso não encontrado")))
                .flatMap(cursoRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteCursosBatch(List<UUID> ids) {
        return cursoRepository.findAllById(ids)
                .collectList()
                .flatMap( curso ->{
                    if (curso.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Nem todos os cursos foram encontrados para os registros: " + ids));
                    }
                    return cursoRepository.deleteAll(curso);
                });
    }


}
