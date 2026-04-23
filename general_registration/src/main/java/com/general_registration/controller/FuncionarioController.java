package com.general_registration.controller;

import com.general_registration.dto.FuncionarioRequestDTO;
import com.general_registration.dto.FuncionarioResponseDTO;
import com.general_registration.model.Funcionario;
import com.general_registration.service.FuncionarioService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/funcionarios")
@RequiredArgsConstructor
@Slf4j
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('FUNCIONARIO_CREATE')")
    public Mono<Void> createFuncionario(@Valid @RequestBody FuncionarioRequestDTO requestDTO) {
        return funcionarioService.createFuncionario(requestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FUNCIONARIO_UPDATE')")
    public Mono<ResponseEntity<Funcionario>> updateFuncionario(@Valid @RequestBody FuncionarioRequestDTO requestDTO, @PathVariable("id") UUID id) {
        return funcionarioService.updateFuncionario(requestDTO, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    public Mono<Page<FuncionarioResponseDTO>> findByFuncionarioFilter(@Valid FuncionarioRequestDTO filter,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "createdAt" ) String sortBy,
                                                                      @RequestParam(defaultValue = "DESC")  Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return funcionarioService.findAllByFuncionarioFilter(filter, pageable);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    public Flux<FuncionarioResponseDTO> findByEmployeeNameOrCpf(@Valid FuncionarioRequestDTO requestDTO,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue ="createdAt") String sortBy,
                                                                @RequestParam(defaultValue = "DESC")  Sort.Direction direction) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return funcionarioService.findByEmployeeNameOrCpf(requestDTO, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    public Mono<ResponseEntity<FuncionarioResponseDTO>> findById(@PathVariable UUID id) {
        return funcionarioService.getFuncionarioById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado")));
    }

    @GetMapping("/pessoa/{idPerson}")
    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    public Mono<ResponseEntity<FuncionarioResponseDTO>> findByIdPerson(@PathVariable UUID idPerson) {
        return funcionarioService.getFuncionarioByIdPerson(idPerson)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FUNCIONARIO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteFuncionario(@PathVariable UUID id) {
        return funcionarioService.deleteFuncionario(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume( e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado")));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('FUNCIONARIO_DELETE')")
    public Mono<ResponseEntity<Object>> deleteFuncionariosBatch(@RequestBody List<UUID> ids) {
        return funcionarioService.deleteFuncionarioBatch(ids)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume( e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionários não encontrados")));
    }
}
