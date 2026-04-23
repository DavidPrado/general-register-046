package com.general_registration.repository;

import com.general_registration.dto.FuncionarioResponseDTO;
import com.general_registration.model.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FuncionarioRepository extends ReactiveCrudRepository<Funcionario, UUID>, ReactiveQueryByExampleExecutor<Funcionario> {

    Mono<Funcionario> findByIdPerson(UUID idPerson);

    @Query(
            """
            SELECT f.id, f.id_person, f.id_enterprise, f.id_position_enterprise, p.name as name_person, p.cpf as cpf_person
            FROM employee f 
            JOIN person p ON f.id_person = p.id 
            WHERE (p.name ILIKE CONCAT('%', :query, '%') OR p.cpf LIKE CONCAT('%', :query, '%'))
            AND (:idEnterprise IS NULL OR f.id_enterprise = :idEnterprise)
            """
    )
    Flux<FuncionarioResponseDTO> findByEmployeeNameOrCpf(String query, UUID idEnterprise, Pageable pageable);
}
