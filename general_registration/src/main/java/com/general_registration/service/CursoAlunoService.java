package com.general_registration.service;

import com.general_registration.dto.CursoAlunoRequestDTO;
import com.general_registration.dto.CursoAlunoResponseDTO;
import com.general_registration.matchers.CursoAlunoMatchers;
import com.general_registration.model.CursoAluno;
import com.general_registration.repository.CursoAlunoRepository;
import lombok.RequiredArgsConstructor;
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
public class CursoAlunoService {

    private final CursoAlunoRepository cursoAlunoRepository;

    @Transactional
    public Mono<Void> createCursoAluno(CursoAlunoRequestDTO requestDTO) {
        return cursoAlunoRepository.save(dtoToCursoAluno(requestDTO)).then();
    }

    private CursoAluno dtoToCursoAluno(CursoAlunoRequestDTO requestDTO) {
        return CursoAluno.builder()
                .idStudent(requestDTO.idStudent())
                .idCourse(requestDTO.idCourse())
                .build();
    }

    public Flux<CursoAlunoResponseDTO> findAllByCursoAlunoFilter(CursoAlunoRequestDTO requestDTO, Pageable pageable) {
        CursoAluno cursoAluno = dtoToCursoAluno(requestDTO);
        ExampleMatcher matcher = CursoAlunoMatchers.listCursoAlunoFilter();
        Example<CursoAluno> example = Example.of(cursoAluno, matcher);

        return cursoAlunoRepository.findAll(example , pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toCursoAlunoResponseDTO);
    }

    private CursoAlunoResponseDTO toCursoAlunoResponseDTO(CursoAluno cursoAluno) {
        return new CursoAlunoResponseDTO(cursoAluno.getId(),
                cursoAluno.getIdStudent(),
                cursoAluno.getIdCourse());
    }

    public Mono<CursoAlunoResponseDTO> findById(UUID id) {
        return cursoAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Curso do Aluno não encontrado")))
                .map(this::toCursoAlunoResponseDTO);
    }

    public Flux<CursoAlunoResponseDTO> findByIdStudent(UUID id) {
        return cursoAlunoRepository.findByIdStudent(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Curso do Aluno não encontrado")))
                .map(this::toCursoAlunoResponseDTO);
    }

    @Transactional
    public Mono<CursoAluno> updateCursoAluno(CursoAlunoRequestDTO requestDTO, UUID id) {
        return cursoAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Curso do Aluno não encontrado")))
                .flatMap(existingCursoAluno -> {
                    existingCursoAluno.setIdStudent(requestDTO.idStudent());
                    existingCursoAluno.setIdCourse(requestDTO.idCourse());
                    return cursoAlunoRepository.save(existingCursoAluno);
                });
    }

    @Transactional
    public Mono<Void> deleteCursoAluno(UUID id) {
        return cursoAlunoRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Curso do Aluno não encontrado")))
            .flatMap(cursoAlunoRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteCursoAlunoBatch(List<UUID> ids) {
        return cursoAlunoRepository.findAllById(ids)
                .collectList()
                .flatMap( cursoAlunos -> {
                    if (cursoAlunos.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Nem todos os Cursos do Aluno encontrados para exclusão"));
                    }
                    return cursoAlunoRepository.deleteAll(cursoAlunos);
                });
    }

    @Transactional
    public Mono<Void> saveBatch(UUID studentId, List<UUID> courseIdsFromRequest) {
        List<UUID> finalCourseIds = (courseIdsFromRequest == null) ? List.of() : courseIdsFromRequest;

        return cursoAlunoRepository.findByIdStudent(studentId)
                .map(CursoAluno::getIdCourse)
                .collectList()
                .flatMap(idsNoBanco -> {

                    List<UUID> idsParaRemover = idsNoBanco.stream()
                            .filter(id -> !finalCourseIds.contains(id))
                            .toList();

                    List<CursoAluno> novosCursos = finalCourseIds.stream()
                            .filter(id -> !idsNoBanco.contains(id))
                            .map(courseId -> CursoAluno.builder()
                                    .idStudent(studentId)
                                    .idCourse(courseId)
                                    .build())
                            .toList();

                    Mono<Void> deleteOperation = idsParaRemover.isEmpty() ?
                            Mono.empty() :
                            cursoAlunoRepository.deleteByIdStudentAndIdCourseIn(studentId, idsParaRemover);

                    Mono<Void> saveOperation = novosCursos.isEmpty() ?
                            Mono.empty() :
                            cursoAlunoRepository.saveAll(novosCursos).then();

                    return Mono.when(deleteOperation, saveOperation);
                });
    }
}
