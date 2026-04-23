package com.general_registration.repository;

import com.general_registration.dto.EstagioVencidoDTO;
import com.general_registration.model.Estagio;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstagioRepository extends ReactiveCrudRepository<Estagio, UUID>, ReactiveQueryByExampleExecutor<Estagio> {

    Mono<Boolean> existsByIdStudentAndIdEnterprise(UUID idStudent, UUID idEnterprise);

    Mono<Estagio> findByIdStudentAndIdEnterprise(UUID idStudent, UUID idEnterprise);

    @Modifying
    @Query("UPDATE internship SET termination_contract = true, " +
            "termination_contract_reason = 'Contrato finalizado conforme a data de termino' " +
            "WHERE end_date < :hoje AND termination_contract = false")
    Mono<Integer> encerrarEstagiosVencidosEmLote(LocalDate hoje);


    @Query("""
               @Query("SELECT e.id AS estagio_id, p.name AS aluno_nome, s.id AS id_student " +
                      "FROM internship e " +
                      "JOIN student s ON e.id_student = s.id " +
                      "JOIN person p ON s.id_person = p.id " +
                      "WHERE e.end_date <= :hoje AND (e.termination_contract IS FALSE OR e.termination_contract IS NULL)")
            """)
    Flux<EstagioVencidoDTO> findEstagiosVencidosComDados(LocalDate dataReferencia);

    @Query("SELECT COUNT(*) FROM internship WHERE end_date >= :hoje AND (termination_contract IS FALSE OR termination_contract IS NULL)")
    Mono<Long> countEstagiosAtivos(LocalDate hoje);

    @Query("SELECT COUNT(*) FROM internship WHERE termination_contract IS FALSE AND end_date BETWEEN :hoje AND :dataFutura")
    Mono<Long> countEstagiosVencendo(LocalDate hoje, LocalDate dataFutura);
}
