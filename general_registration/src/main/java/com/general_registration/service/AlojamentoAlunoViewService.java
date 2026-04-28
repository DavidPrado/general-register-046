package com.general_registration.service;

import com.general_registration.dto.AlojamentoAlunoViewRequestDTO;
import com.general_registration.dto.AlojamentoAlunoViewResponseDTO;
import com.general_registration.matchers.AlojamentoAlunoViewMatcher;
import com.general_registration.model.AlojamentoAlunoView;
import com.general_registration.repository.AlojamentoAlunoViewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AlojamentoAlunoViewService {

    private final AlojamentoAlunoViewRepository alojamentoAlunoViewRepository;

    public Mono<Page<AlojamentoAlunoViewResponseDTO>> findAllAlojamentoAlunoViews(AlojamentoAlunoViewRequestDTO requestDTO, Pageable pageable) {

        AlojamentoAlunoView filterEntity = toAlojamentoAlunoView(requestDTO);

        Example<AlojamentoAlunoView> example = Example.of(
                filterEntity,
                AlojamentoAlunoViewMatcher.listAlojamentoAlunoViewFilter()
        );

        return alojamentoAlunoViewRepository.findAll(example)
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toAlojamentoAlunoViewResponseDTO)
                .collectList()
                .zipWith(alojamentoAlunoViewRepository.count(example)) // Conta o total para o PageImpl
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private AlojamentoAlunoViewResponseDTO toAlojamentoAlunoViewResponseDTO(AlojamentoAlunoView alojamentoAlunoView) {
        return new AlojamentoAlunoViewResponseDTO(
                alojamentoAlunoView.getId(),
                alojamentoAlunoView.getRoomNumber(),
                alojamentoAlunoView.getBuildingBlock(),
                alojamentoAlunoView.getMaxCapacity(),
                alojamentoAlunoView.getGenderType(),
                alojamentoAlunoView.getStudentName(),
                alojamentoAlunoView.getEntryDate(),
                alojamentoAlunoView.getExitDate(),
                alojamentoAlunoView.getStatusAccommodation()
        );
    }

    private AlojamentoAlunoView toAlojamentoAlunoView(AlojamentoAlunoViewRequestDTO requestDTO) {
        return AlojamentoAlunoView.builder()
                .id(requestDTO.id())
                .roomNumber(requestDTO.roomNumber())
                .buildingBlock(requestDTO.buildingBlock())
                .maxCapacity(requestDTO.maxCapacity())
                .genderType(requestDTO.genderType())
                .studentName(requestDTO.studentName())
                .entryDate(requestDTO.entryDate())
                .exitDate(requestDTO.exitDate())
                .statusAccommodation(requestDTO.statusAccommodation())
                .build();
    }
}
