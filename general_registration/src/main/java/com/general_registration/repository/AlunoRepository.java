package com.general_registration.repository;

import com.general_registration.dto.AlunoResponseDTO;
import com.general_registration.model.Aluno;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AlunoRepository extends ReactiveCrudRepository<Aluno, UUID>, ReactiveQueryByExampleExecutor<Aluno> {

    Mono<Aluno> findByIdPerson(UUID idPerson);

    Mono<Aluno> findByIdPersonAndIdSchool(UUID idPerson, UUID idSchool);

    Mono<Boolean> existsByIdPersonAndIdSchool(UUID idPerson, UUID idSchool);

    @Query("""
        SELECT a.id, a.id_person, a.id_school, p.name as nome_pessoa, p.cpf as cpf_pessoa 
        FROM student a 
        JOIN person p ON a.id_person = p.id 
        WHERE (p.name ILIKE CONCAT('%', :query, '%') OR p.cpf LIKE CONCAT('%', :query, '%'))
        AND (:idSchool IS NULL OR a.id_school = :idSchool)
    """)
    Flux<AlunoResponseDTO> findByPersonNameOrCpf(String query, UUID idSchool, Pageable pageable);
}
