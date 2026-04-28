package com.general_registration.repository;

import com.general_registration.model.AlojamentoAlunoView;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlojamentoAlunoViewRepository  extends ReactiveCrudRepository<AlojamentoAlunoView, UUID>,
        ReactiveQueryByExampleExecutor<AlojamentoAlunoView> {
}
