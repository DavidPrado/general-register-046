package com.general_registration.repository;

import com.general_registration.model.Pessoa;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;
import java.util.UUID;

@Repository
public interface PessoaRepository extends ReactiveCrudRepository<Pessoa, UUID>, ReactiveQueryByExampleExecutor<Pessoa> {

    Flux<Pessoa> findAllBy(Pageable pageable);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByCpf(String cpf);

    Mono<Boolean> existsByRg(String rg);
}
