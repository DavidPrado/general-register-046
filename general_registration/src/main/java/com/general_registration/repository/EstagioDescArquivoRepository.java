package com.general_registration.repository;

import com.general_registration.model.EstagioDescArquivo;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EstagioDescArquivoRepository extends ReactiveCrudRepository<EstagioDescArquivo, UUID>, ReactiveQueryByExampleExecutor<EstagioDescArquivo> {

    Flux<EstagioDescArquivo> findAllByIdInternship(UUID idInternship);

    Mono<Object> deleteAllByIdInternship(UUID id);
}
