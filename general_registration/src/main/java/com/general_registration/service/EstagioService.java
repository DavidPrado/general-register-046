package com.general_registration.service;

import com.general_registration.dto.EstagioRequestDTO;
import com.general_registration.dto.EstagioResponseDTO;
import com.general_registration.matchers.EstagioMatcher;
import com.general_registration.model.Estagio;
import com.general_registration.repository.EstagioArquivoRepository;
import com.general_registration.repository.EstagioDescArquivoRepository;
import com.general_registration.repository.EstagioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EstagioService {

    private final EstagioRepository estagioRepository;
    private final EstagioArquivoRepository estagioArquivoRepository;
    private final EstagioDescArquivoRepository estagioDescArquivoRepository;

    @Transactional
    public Mono<EstagioResponseDTO> createEstagio(EstagioRequestDTO requestDTO) {
        return validateEstagioUniqueness(requestDTO)
                .then(Mono.defer(() -> {
                    Estagio estagio = Estagio.builder()
                            .idStudent(requestDTO.idStudent())
                            .idEnterprise(requestDTO.idEnterprise())
                            .idEmployeeSupervisor(requestDTO.idEmployeeSupervisor())
                            .startDate(requestDTO.startDate())
                            .endDate(requestDTO.endDate())
                            .internshipSalary(requestDTO.intershipSalary())
                            .hoursPerWeek(requestDTO.hoursPerWeek())
                            .hoursPerDay(requestDTO.hoursPerDay())
                            .terminationContract(requestDTO.terminationContract())
                            .terminationContractReason(requestDTO.terminationContractReason())
                            .build();
                    return estagioRepository.save(estagio);
                })).map(this::toEstagioResponseDTO);
    }

    private Mono<Void> validateEstagioUniqueness(EstagioRequestDTO requestDTO) {
        return estagioRepository.existsByIdStudentAndIdEnterprise(requestDTO.idStudent(), requestDTO.idEnterprise())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Aluno já possui estágio nessa empresa"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> validateEstagioUniquenessUpdate(EstagioRequestDTO requestDTO, UUID id) {
        return estagioRepository.findByIdStudentAndIdEnterprise(requestDTO.idStudent(), requestDTO.idEnterprise())
                .flatMap(estagio -> {
                    if (estagio != null
                            && !estagio.getId().equals(id)
                            && estagio.getIdStudent().equals(requestDTO.idStudent())
                            && estagio.getIdEnterprise().equals(requestDTO.idEnterprise())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Aluno já possui estágio nessa empresa"));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Page<EstagioResponseDTO>> findAllByEstagioFilter(EstagioRequestDTO requestDTO, Pageable pageable) {
        Estagio estagio = toEstagio(requestDTO);
        ExampleMatcher exampleMatcher = EstagioMatcher.listEstagioFilter();
        Example<Estagio> example = Example.of(estagio, exampleMatcher);

        Mono<List<EstagioResponseDTO>> data = estagioRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toEstagioResponseDTO)
                .collectList();

        Mono<Long> total = estagioRepository.count(example);

        return Mono.zip(data, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));

    }

    private EstagioResponseDTO toEstagioResponseDTO(Estagio estagio) {
        return new EstagioResponseDTO(
                estagio.getId(),
                estagio.getIdStudent(),
                estagio.getIdEnterprise(),
                estagio.getIdEmployeeSupervisor(),
                estagio.getStartDate(),
                estagio.getEndDate(),
                estagio.getInternshipSalary(),
                estagio.getHoursPerWeek(),
                estagio.getHoursPerDay(),
                estagio.getTerminationContract(),
                estagio.getTerminationContractReason(),
                null,
                null,
                null,
                null
        );

    }

    private Estagio toEstagio(EstagioRequestDTO requestDTO) {
        return Estagio.builder()
                .idStudent(requestDTO.idStudent())
                .idEnterprise(requestDTO.idEnterprise())
                .idEmployeeSupervisor(requestDTO.idEmployeeSupervisor())
                .startDate(requestDTO.startDate())
                .endDate(requestDTO.endDate())
                .internshipSalary(requestDTO.intershipSalary())
                .hoursPerWeek(requestDTO.hoursPerWeek())
                .hoursPerDay(requestDTO.hoursPerDay())
                .terminationContract(requestDTO.terminationContract())
                .terminationContractReason(requestDTO.terminationContractReason())
                .build();
    }

    @Transactional
    public Mono<Estagio> updateEstagio(UUID id, EstagioRequestDTO requestDTO) {
        return estagioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Estágio não encontrado")))
                .flatMap(estagio -> Mono.when(validateEstagioUniquenessUpdate(requestDTO, id))
                        .then(Mono.defer(() -> {
                            estagio.setIdStudent(requestDTO.idStudent());
                            estagio.setIdEnterprise(requestDTO.idEnterprise());
                            estagio.setIdEmployeeSupervisor(requestDTO.idEmployeeSupervisor());
                            estagio.setStartDate(requestDTO.startDate());
                            estagio.setEndDate(requestDTO.endDate());
                            estagio.setInternshipSalary(requestDTO.intershipSalary());
                            estagio.setHoursPerWeek(requestDTO.hoursPerWeek());
                            estagio.setHoursPerDay(requestDTO.hoursPerDay());
                            estagio.setTerminationContract(requestDTO.terminationContract());
                            estagio.setTerminationContractReason(requestDTO.terminationContractReason());
                            return estagioRepository.save(estagio);
                        }))
                );
    }

    public Mono<EstagioResponseDTO> getEstagioById(UUID id) {
        return estagioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Estágio não encontrado")))
                .map(this::toEstagioResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteEstagio(UUID id) {
        return estagioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Estágio não encontrado")))
                .flatMap(estagio -> {
                    estagioDescArquivoRepository.findAllByIdInternship(estagio.getId())
                            .flatMap(meta -> estagioArquivoRepository.deleteById(meta.getId()))
                            .then(estagioDescArquivoRepository.deleteAllByIdInternship(estagio.getId()))
                            .then(estagioRepository.delete(estagio));
                    return Mono.empty();
                });
    }

    @Transactional
    public Mono<Void> deleteEstagiosBatch(List<UUID> ids) {
        return estagioRepository.findAllById(ids)
                .collectList()
                .flatMap(estagios -> {
                    if (estagios.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Um ou mais estágios não encontrados"));
                    }
                    return Flux.fromIterable(estagios)
                            .flatMap(estagio ->
                                    estagioDescArquivoRepository.findAllByIdInternship(estagio.getId())
                                            .flatMap(meta ->
                                                    estagioArquivoRepository.deleteById(meta.getId())
                                                            .then(estagioDescArquivoRepository.deleteById(meta.getId()))
                                            )
                                            .then(estagioRepository.delete(estagio))
                            )
                            .then();
                });
    }

    @Transactional
    public Mono<Estagio> saveAndUpdate(EstagioRequestDTO requestDTO) {
        if (requestDTO == null) return Mono.empty();

        return estagioRepository.findByIdStudentAndIdEnterprise(requestDTO.idStudent(), requestDTO.idEnterprise())
                .flatMap(existingEstagio -> updateEstagio(existingEstagio.getId(), requestDTO)
                        .then(estagioRepository.findById(existingEstagio.getId()))
                        .switchIfEmpty(Mono.defer(() -> {
                            Estagio novoEstagio = toEstagio(requestDTO);
                            return estagioRepository.save(novoEstagio);
                        })));
    }

    @Transactional
    public Mono<Void> saveAndUpdateBatch(List<EstagioRequestDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return Mono.empty();

        var estagiosNovos = dtos.stream()
                .filter(d -> d.id() == null)
                .map(d -> Estagio.builder()
                        .idStudent(d.idStudent())
                        .idEnterprise(d.idEnterprise())
                        .idEmployeeSupervisor(d.idEmployeeSupervisor())
                        .startDate(d.startDate())
                        .endDate(d.endDate())
                        .internshipSalary(d.intershipSalary())
                        .hoursPerWeek(d.hoursPerWeek())
                        .hoursPerDay(d.hoursPerDay())
                        .terminationContract(d.terminationContract())
                        .terminationContractReason(d.terminationContractReason())
                        .build())
                .toList();

        var estagiosExistentes = dtos.stream()
                .filter(d -> d.id() != null)
                .map(d -> Estagio.builder()
                        .idStudent(d.idStudent())
                        .idEnterprise(d.idEnterprise())
                        .idEmployeeSupervisor(d.idEmployeeSupervisor())
                        .startDate(d.startDate())
                        .endDate(d.endDate())
                        .internshipSalary(d.intershipSalary())
                        .hoursPerWeek(d.hoursPerWeek())
                        .hoursPerDay(d.hoursPerDay())
                        .terminationContract(d.terminationContract())
                        .terminationContractReason(d.terminationContractReason())
                        .build())
                .toList();

        Mono<Void> insertFlux = estagioRepository.saveAll(estagiosNovos).then();
        Mono<Void> updateFlux = estagioRepository.saveAll(estagiosExistentes).then();

        return Mono.when(insertFlux, updateFlux);
    }

}
