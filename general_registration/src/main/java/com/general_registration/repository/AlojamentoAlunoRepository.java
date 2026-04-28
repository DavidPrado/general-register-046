package com.general_registration.repository;

import com.general_registration.model.AlojamentoAluno;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AlojamentoAlunoRepository extends ReactiveCrudRepository<AlojamentoAluno, UUID>, ReactiveQueryByExampleExecutor<AlojamentoAluno> {
    Mono<Boolean> existsByIdAccommodationAndIdStudent(UUID idAccommodation, UUID idStudent);

    Mono<AlojamentoAluno> findByIdAccommodationAndIdStudent(UUID id, UUID uuid);
}
