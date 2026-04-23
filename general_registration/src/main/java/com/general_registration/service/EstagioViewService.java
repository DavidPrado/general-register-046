package com.general_registration.service;

import com.general_registration.dto.EstagioRequestListDTO;
import com.general_registration.dto.EstagioResponseDTO;
import com.general_registration.matchers.EstagioMatcher;
import com.general_registration.model.EstagioView;
import com.general_registration.repository.EstagioViewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EstagioViewService {

    private final EstagioViewRepository estagioViewRepository;

    public Mono<Page<EstagioResponseDTO>> findAllByEstagioViewFilter(EstagioRequestListDTO requestDTO, Pageable pageable) {

        if (Boolean.TRUE.equals(requestDTO.vencimento30Dias())){
            return estagioViewRepository.findVencendoProximos30Dias(pageable.getPageSize(), pageable.getOffset())
                    .map(this::toResponseDTO)
                    .collectList()
                    .zipWith(estagioViewRepository.countVencendoProximos30Dias())
                    .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
        }

        EstagioView filterEntity = toEstagioView(requestDTO);
        Example<EstagioView> example = Example.of(filterEntity, EstagioMatcher.listViewFilter());

        return estagioViewRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toResponseDTO)
                .collectList()
                .zipWith(estagioViewRepository.count(example))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    public Mono<EstagioResponseDTO> findById(UUID id) {
        return estagioViewRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estágio não encontrado")))
                .map(this::toResponseDTO);
    }

    private EstagioView toEstagioView(EstagioRequestListDTO requestDTO) {
        return EstagioView.builder()
                .id(requestDTO.id())
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
                .studentName(requestDTO.studentName())
                .studentCpf(requestDTO.studentCpf())
                .enterpriseName(requestDTO.enterpriseName())
                .enterpriseCnpj(requestDTO.enterpriseCnpj())
                .build();
    }

    private EstagioResponseDTO toResponseDTO(EstagioView estagioView) {
        return new EstagioResponseDTO(
                estagioView.getId(),
                estagioView.getIdStudent(),
                estagioView.getIdEnterprise(),
                estagioView.getIdEmployeeSupervisor(),
                estagioView.getStartDate(),
                estagioView.getEndDate(),
                estagioView.getInternshipSalary(),
                estagioView.getHoursPerWeek(),
                estagioView.getHoursPerDay(),
                estagioView.getTerminationContract(),
                estagioView.getTerminationContractReason(),
                estagioView.getStudentName(),
                estagioView.getStudentCpf(),
                estagioView.getEnterpriseName(),
                estagioView.getEnterpriseCnpj()
        );
    }
}
