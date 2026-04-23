package com.general_registration.repository;

import com.general_registration.model.ResponsavelAluno;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ResponsavelAlunoRepository extends ReactiveCrudRepository<ResponsavelAluno, UUID>, ReactiveQueryByExampleExecutor<ResponsavelAluno> {


    Flux<ResponsavelAluno> findByIdStudent(UUID idStudent);

    Mono<Void> deleteByIdStudent(UUID id);
}
