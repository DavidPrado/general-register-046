package com.general_registration.repository;

import com.general_registration.model.Escola;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EscolaRepository extends ReactiveCrudRepository<Escola, UUID>, ReactiveQueryByExampleExecutor<Escola> {

    Mono<Boolean> existsByCode(String code);

    Mono<Boolean> existsByCodeAndIdNot(String code, UUID id);

}
