package com.general_registration.repository;

import com.general_registration.model.Curso;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CursoRepository extends ReactiveCrudRepository<Curso, UUID>, ReactiveQueryByExampleExecutor<Curso> {
}
