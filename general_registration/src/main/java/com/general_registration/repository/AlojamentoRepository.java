package com.general_registration.repository;

import com.general_registration.model.Alojamento;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AlojamentoRepository extends ReactiveCrudRepository<Alojamento, UUID>, ReactiveQueryByExampleExecutor<Alojamento> {

    Mono<Boolean> existsByRoomNumberAndBuildingBlock(String roomNumber, String buildingBlock);

}
