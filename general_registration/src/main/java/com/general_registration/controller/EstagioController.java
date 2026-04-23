package com.general_registration.controller;

import com.general_registration.dto.EstagioRequestDTO;
import com.general_registration.dto.EstagioRequestListDTO;
import com.general_registration.dto.EstagioResponseDTO;
import com.general_registration.model.Estagio;
import com.general_registration.service.EstagioService;
import com.general_registration.service.EstagioViewService;
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
@RequestMapping("/api/estagios")
@RequiredArgsConstructor
@Slf4j
public class EstagioController {

    private final EstagioService estagioService;
    private final EstagioViewService estagioViewService;


    @PostMapping
    @PreAuthorize("hasAuthority('ESTAGIO_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EstagioResponseDTO> createEstagio(@Valid @RequestBody EstagioRequestDTO requestDTO) {
        return estagioService.createEstagio(requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ESTAGIO_UPDATE')")
    public Mono<ResponseEntity<Estagio>> updateEstagio(@PathVariable UUID id, @Valid @RequestBody EstagioRequestDTO requestDTO) {
        return estagioService.updateEstagio(id, requestDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estágio não encontrado")));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ESTAGIO_READ')")
    public Mono<ResponseEntity<EstagioResponseDTO>> getEstagioById(@PathVariable UUID id) {
        return estagioViewService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estágio não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ESTAGIO_READ')")
    public Mono<Page<EstagioResponseDTO>> getEstagiosByFilter(EstagioRequestDTO filter,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(defaultValue = "DESC") String direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return estagioService.findAllByEstagioFilter(filter, pageable);
    }

    @GetMapping("/view")
    @PreAuthorize("hasAuthority('ESTAGIO_READ')")
    public Mono<Page<EstagioResponseDTO>> getEstagiosViewByFilter(EstagioRequestListDTO filter,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                  @RequestParam(defaultValue = "DESC") String direction){
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Sort.Direction sortDir = Sort.Direction.DESC;
        try {
            sortDir = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
           return Mono.error(new RuntimeException("Direção de ordenação inválida. Use 'ASC' ou 'DESC'."));
        }
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(sortDir, sortBy));
        return estagioViewService.findAllByEstagioViewFilter(filter, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ESTAGIO_DELETE')")
    public Mono<Void> deleteEstagio(@PathVariable UUID id) {
        return estagioService.deleteEstagio(id);
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ESTAGIO_DELETE')")
    public Mono<Void> deleteEstagiosBatch(@RequestBody List<UUID> ids) {
        return estagioService.deleteEstagiosBatch(ids);
    }

    @PostMapping("/save-or-update")
    @PreAuthorize("hasAuthority('ESTAGIO_CREATE') or hasAuthority('ESTAGIO_UPDATE')")
    public Mono<Estagio> upsertEstagios(@RequestBody EstagioRequestDTO estagioRequestDTOs) {
        return estagioService.saveAndUpdate(estagioRequestDTOs);
    }

    @PostMapping("/save-or-update-batch")
    @PreAuthorize("hasAuthority('ESTAGIO_CREATE') or hasAuthority('ESTAGIO_UPDATE')")
    public Mono<Void> upsertEstagios(@RequestBody List<EstagioRequestDTO> estagioRequestDTOs) {
        return estagioService.saveAndUpdateBatch(estagioRequestDTOs);
    }
}
