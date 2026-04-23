package com.general_registration.repository;

import com.general_registration.model.EstagioView;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EstagioViewRepository extends ReactiveCrudRepository<EstagioView, UUID>,
        ReactiveQueryByExampleExecutor<EstagioView> {

    @Query("""
        SELECT * FROM v_internship_complete 
        WHERE end_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '30 days')
        ORDER BY end_date ASC
        LIMIT :size OFFSET :offset
    """)
    Flux<EstagioView> findVencendoProximos30Dias(int size, long offset);

    @Query("SELECT COUNT(*) FROM v_internship_complete WHERE end_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '30 days')")
    Mono<Long> countVencendoProximos30Dias();
}
