package com.general_registration.service;

import com.general_registration.dto.EstagioDescArquivoRequestDTO;
import com.general_registration.dto.EstagioDescArquivoResponseDTO;
import com.general_registration.matchers.EstagioArquivoMatchers;
import com.general_registration.model.EstagioDescArquivo;
import com.general_registration.repository.EstagioDescArquivoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EstagioDescArquivoService {

    private final EstagioDescArquivoRepository estagioArquivoRepository;

    @Transactional
    public Mono<EstagioDescArquivoResponseDTO> createEstagioArquivo(EstagioDescArquivoRequestDTO requestDTO) {
        return estagioArquivoRepository.save(toEstagioArquivo(requestDTO))
                .map(this::toEstagioArquivoResponseDTO);
    }

    private EstagioDescArquivo toEstagioArquivo(EstagioDescArquivoRequestDTO requestDTO) {
        return EstagioDescArquivo.builder()
                .idInternship(requestDTO.idInternship())
                .fileName(requestDTO.fileName())
                .fileSize(requestDTO.fileSize())
                .fileDescription(requestDTO.fileDescription())
                .build();
    }

    private EstagioDescArquivoResponseDTO toEstagioArquivoResponseDTO(EstagioDescArquivo estagioArquivo) {
        return new EstagioDescArquivoResponseDTO(estagioArquivo.getId(),
                estagioArquivo.getIdInternship(),
                estagioArquivo.getFileName(),
                estagioArquivo.getFileDescription(),
                estagioArquivo.getFileSize(),
                estagioArquivo.getMimeType());
    }

    public Mono<Page<EstagioDescArquivoResponseDTO>> findAllByEstagioArquivoFilter(EstagioDescArquivoRequestDTO requestDTO, Pageable pageable) {
        EstagioDescArquivo estagioArquivo = toEstagioArquivo(requestDTO);
        ExampleMatcher matcher = EstagioArquivoMatchers.listEstagioArquivo();
        Example<EstagioDescArquivo> example = Example.of(estagioArquivo, matcher);


        Mono<List<EstagioDescArquivoResponseDTO>> data = estagioArquivoRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toEstagioArquivoResponseDTO)
                .collectList();

        Mono<Long> total = estagioArquivoRepository.count(example);

        return Mono.zip(data, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    @Transactional
    public Mono<EstagioDescArquivoResponseDTO> updateEstagioArquivo(UUID id, EstagioDescArquivoRequestDTO requestDTO) {
        return estagioArquivoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Arquivo de estágio não encontrado")))
                .flatMap(existing -> {
                    existing.setFileName(requestDTO.fileName());
                    existing.setFileSize(requestDTO.fileSize());
                    existing.setFileDescription(requestDTO.fileDescription());
                    return estagioArquivoRepository.save(existing);
                })
                .map(this::toEstagioArquivoResponseDTO);
    }

    public Mono<EstagioDescArquivoResponseDTO> getEstagioArquivoById(UUID id) {
        return estagioArquivoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Arquivo de estágio não encontrado")))
                .map(this::toEstagioArquivoResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteEstagioArquivo(UUID id) {
        return estagioArquivoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Arquivo de estágio não encontrado")))
                .flatMap(estagioArquivoRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteEstagioArquivoBatch(List<UUID> ids) {
        return estagioArquivoRepository.findAllById(ids)
                .collectList()
                .flatMap(estagioArquivos -> {
                    if (estagioArquivos.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Nem todos os arquivos de estágio foram encontrados para os registros: " + ids));
                    }
                    return estagioArquivoRepository.deleteAll(estagioArquivos);
                });
    }




    public Flux<EstagioDescArquivoResponseDTO> findAllByInternshipId(UUID idInternship) {
        return estagioArquivoRepository.findAllByIdInternship(idInternship)
                .map(this::toEstagioArquivoResponseDTO);
    }
}
