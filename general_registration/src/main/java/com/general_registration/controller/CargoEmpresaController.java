package com.general_registration.controller;

import com.general_registration.dto.CargoEmpresaRequestDTO;
import com.general_registration.dto.CargoEmpresaResponseDTO;
import com.general_registration.model.CargoEmpresa;
import com.general_registration.service.CargoEmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cargos")
@RequiredArgsConstructor
@Slf4j
public class CargoEmpresaController {

    private final CargoEmpresaService cargoEmpresaService;

    @PostMapping
    @PreAuthorize("hasAuthority('CARGO_EMPRESA_CREATE')")
    public Mono<Void> createCargoEmpresa(@Valid @RequestBody CargoEmpresaRequestDTO cargoEmpresaRequestDTO) {
       return cargoEmpresaService.createCargoEmpresa(cargoEmpresaRequestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CARGO_EMPRESA_UPDATE')")
    public Mono<ResponseEntity<CargoEmpresa>> updateCargoEmpresa(@Valid @RequestBody CargoEmpresaRequestDTO cargoEmpresaRequestDTO, @PathVariable("id") UUID id) {
        return cargoEmpresaService.updateCargoEmpresa(cargoEmpresaRequestDTO, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CARGO_EMPRESA_READ')")
    public Mono<Page<CargoEmpresaResponseDTO>> getCargoEmpresaFilter(CargoEmpresaRequestDTO filter,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                     @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return cargoEmpresaService.findAllByCargoEmpresaFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CARGO_EMPRESA_READ')")
    public Mono<ResponseEntity<CargoEmpresaResponseDTO>> getCargoEmpresaById(@PathVariable UUID id) {
        return  cargoEmpresaService.getCargoEmpresaById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado")));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CARGO_EMPRESA_DELETE')")
    public Mono<ResponseEntity<Object>> deleteCargoEmpresa(@PathVariable UUID id) {
        return cargoEmpresaService.deleteCargoEmpresa(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume( e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('CARGO_EMPRESA_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCargoEmpresaBatch(@RequestBody List<UUID> ids) {
        return cargoEmpresaService.deleteCargoEmpresaBatch(ids);
    }
}
