package com.general_registration.repository;

import com.general_registration.model.Empresa;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EmpresaRepository extends ReactiveCrudRepository<Empresa, UUID>, ReactiveQueryByExampleExecutor<Empresa> {

    Mono<Boolean> existsByCnpj(String cnpj);

    Mono<Boolean> existsByCnpjAndIdNot(String cnpj, UUID id);
}
