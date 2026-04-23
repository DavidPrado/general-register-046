package com.general_registration.repository;

import com.general_registration.model.CursoAluno;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface CursoAlunoRepository extends ReactiveCrudRepository<CursoAluno, UUID>, ReactiveQueryByExampleExecutor<CursoAluno> {

    Flux<CursoAluno> findByIdStudent(UUID IdStudent);

    @Modifying
    @Query("DELETE FROM student_course WHERE id_student = :studentId AND id_course IN (:courseIds)")
    Mono<Void> deleteByIdStudentAndIdCourseIn(UUID studentId, List<UUID> idsParaRemover);

    Mono<Void> deleteByIdStudent(UUID id);
}
