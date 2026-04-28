package com.general_registration.service;


import com.general_registration.dto.AlogamentoBatchRequestDTO;
import com.general_registration.dto.AlojamentoRequestDTO;
import com.general_registration.dto.AlojamentoResponseDTO;
import com.general_registration.matchers.AlojamentoMatchers;
import com.general_registration.model.Alojamento;
import com.general_registration.repository.AlojamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlojamentoService {

    private final AlojamentoRepository alojamentoRepository;


    @Transactional
    public Mono<Alojamento> createAlojamento(AlojamentoRequestDTO alojamento) {
        return alojamentoRepository.existsByRoomNumberAndBuildingBlock(alojamento.roomNumber(), alojamento.buildingBlock())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new RuntimeException("Alojamento já existe para o número do quarto e bloco fornecidos"));
                    }
                    return alojamentoRepository.save(dtoToAlojamento(alojamento));
                });
    }

    @Transactional
    public Mono<Void> createAlojamentoBatch(AlogamentoBatchRequestDTO alojamentoBatch) {
        int start = alojamentoBatch.initialNumberRoom();
        int end = alojamentoBatch.finalNumberRoom();
        int total = (end - start) + 1;

        return Flux.range(start, total)
                .map(currentNumber -> mapToEntity(alojamentoBatch, currentNumber))
                .collectList()
                .flatMapMany(alojamentoRepository::saveAll)
                .then();
    }

    private Alojamento mapToEntity(AlogamentoBatchRequestDTO request, int number) {
        Alojamento entity = new Alojamento();
        entity.setRoomNumber(request.prefixRoom() + number);
        entity.setBuildingBlock(request.buildingBlock());
        entity.setMaxCapacity(request.maxCapacity());
        entity.setSquareMeters(request.squareMeters());
        entity.setGenderType(request.genderType());
        entity.setStatus(request.status());
        return entity;
    }

    private Alojamento dtoToAlojamento(AlojamentoRequestDTO requestDTO) {
        return Alojamento.builder()
                .roomNumber(requestDTO.roomNumber())
                .buildingBlock(requestDTO.buildingBlock())
                .maxCapacity(requestDTO.maxCapacity())
                .squareMeters(requestDTO.squareMeters())
                .genderType(requestDTO.genderType())
                .status(requestDTO.status())
                .build();
    }

    public Mono<Page<AlojamentoResponseDTO>> findAllByAlojamentoFilter(AlojamentoRequestDTO dto, Pageable pageable) {
        Alojamento alojamento = dtoToAlojamento(dto);
        ExampleMatcher matcher = AlojamentoMatchers.listAlojamentoFilter();
        Example<Alojamento> example = Example.of(alojamento, matcher);

        Mono<List<AlojamentoResponseDTO>> data = alojamentoRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toAlojamentoResponseDTO)
                .collectList();
        Mono<Long> total = alojamentoRepository.count(example);

        return Mono.zip(data, total).map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));

    }

    private AlojamentoResponseDTO toAlojamentoResponseDTO(Alojamento alojamento) {
        return new AlojamentoResponseDTO(alojamento.getId(),
                alojamento.getRoomNumber(), alojamento.getBuildingBlock(),
                alojamento.getMaxCapacity(), alojamento.getSquareMeters(),
                alojamento.getGenderType(), alojamento.getStatus());
    }

    @Transactional
    public Mono<Alojamento> updateAlojamento(UUID id, AlojamentoRequestDTO request) {
        return alojamentoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento não encontrado")))
                .flatMap(alojamento -> {
                    return updateAndSaveAlojamento(alojamento,request);
                });
    }

    private Mono<Alojamento> updateAndSaveAlojamento(Alojamento alojamento, AlojamentoRequestDTO request) {
        alojamento.setRoomNumber(request.roomNumber());
        alojamento.setBuildingBlock(request.buildingBlock());
        alojamento.setMaxCapacity(request.maxCapacity());
        alojamento.setSquareMeters(request.squareMeters());
        alojamento.setGenderType(request.genderType());
        alojamento.setStatus(request.status());
        return alojamentoRepository.save(alojamento);
    }

    public Mono<AlojamentoResponseDTO> findById(UUID id) {
        return alojamentoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento não encontrado")))
                .map(this::toAlojamentoResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteAlojamento(UUID id) {
        return alojamentoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento não encontrado")))
                .flatMap(alojamentoRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteAlogamentoBatch(List<UUID> ids) {
        return alojamentoRepository.findAllById(ids)
                .collectList()
                .flatMap(alojamentos -> {
                    if (alojamentos.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Um ou mais alojamentos não encontrados para os IDs fornecidos"));

                    }
                    return alojamentoRepository.deleteAll(alojamentos);
                });
    }


}
