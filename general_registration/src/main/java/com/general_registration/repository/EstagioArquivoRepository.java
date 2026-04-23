package com.general_registration.repository;


import com.general_registration.model.EstagioArquivo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EstagioArquivoRepository extends ReactiveCrudRepository<EstagioArquivo, UUID> {

}
