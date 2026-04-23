package com.general_registration.service;

import com.general_registration.dto.CargoEmpresaRequestDTO;
import com.general_registration.dto.CargoEmpresaResponseDTO;
import com.general_registration.matchers.CargoEmpresaMatchers;
import com.general_registration.model.CargoEmpresa;
import com.general_registration.repository.CargoEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CargoEmpresaService {

    private final CargoEmpresaRepository cargoEmpresaRepository;

    @Transactional
    public Mono<Void> createCargoEmpresa(CargoEmpresaRequestDTO requestDTO) {
        return cargoEmpresaRepository.save(doToCargoEmpresa(requestDTO)).then();
    }

    private CargoEmpresa doToCargoEmpresa(CargoEmpresaRequestDTO requestDTO) {
        return CargoEmpresa.builder()
                .position(requestDTO.position())
                .description(requestDTO.description())
                .active(requestDTO.active())
                .build();
    }

    public  Mono<Page<CargoEmpresaResponseDTO>> findAllByCargoEmpresaFilter(CargoEmpresaRequestDTO requestDTO, Pageable pageable) {
        CargoEmpresa cargoEmpresa = doToCargoEmpresa(requestDTO);
        ExampleMatcher matcher = CargoEmpresaMatchers.listCargoEmpresaFilter();
        Example<CargoEmpresa> example = Example.of(cargoEmpresa, matcher);

        Mono<List<CargoEmpresaResponseDTO>> data = cargoEmpresaRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toCargoEmpresaResponseDTO)
                .collectList();

        Mono<Long> total = cargoEmpresaRepository.count(example);

        return Mono.zip(data, total)
                .map(tuple -> new org.springframework.data.domain.PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));

    }

    private CargoEmpresaResponseDTO toCargoEmpresaResponseDTO(CargoEmpresa cargoEmpresa) {
        return new CargoEmpresaResponseDTO(cargoEmpresa.getId(),
                cargoEmpresa.getPosition(),
                cargoEmpresa.getDescription(),
                cargoEmpresa.getActive());

    }

    @Transactional
    public Mono<CargoEmpresa> updateCargoEmpresa(CargoEmpresaRequestDTO requestDTO, UUID id) {
        return cargoEmpresaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("O cadastro do cargo de funcionario não foi encontrado para o registro: " + id)))
                .flatMap(existingCargoEmpresa -> {
                    existingCargoEmpresa.setPosition(requestDTO.position());
                    existingCargoEmpresa.setDescription(requestDTO.description());
                    existingCargoEmpresa.setActive(requestDTO.active());
                    return cargoEmpresaRepository.save(existingCargoEmpresa);
                });
    }

    public Mono<CargoEmpresaResponseDTO> getCargoEmpresaById(UUID id) {
        return cargoEmpresaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("O cadastro do cargo de funcionario não foi encontrado para o registro: " + id)))
                .map(this::toCargoEmpresaResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteCargoEmpresa(UUID id) {
        return cargoEmpresaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("O cadastro do cargo de funcionario não foi encontrado para o registro: " + id)))
                .flatMap(cargoEmpresaRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteCargoEmpresaBatch(List<UUID> ids) {
        return cargoEmpresaRepository.findAllById(ids)
                .collectList()
                .flatMap( cargoEmpresas -> {
                    if (cargoEmpresas.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Nem todos os cadastros de cargo de funcionario foram encontrados para os registros: " + ids));
                    }
                    return cargoEmpresaRepository.deleteAll(cargoEmpresas);
                });
    }
}
