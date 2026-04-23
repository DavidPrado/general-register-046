package com.general_registration.controller;

import com.general_registration.dto.PessoaRequestDTO;
import com.general_registration.dto.PessoaResponseDTO;
import com.general_registration.dto.PessoaSearchRequestDTO;
import com.general_registration.model.Pessoa;
import com.general_registration.service.PessoaService;
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
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
@Slf4j
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    @PreAuthorize("hasAuthority('PESSOA_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PessoaResponseDTO> createPessoa(@Valid @RequestBody PessoaRequestDTO pessoaRequestDTO) {
        return pessoaService.createPessoa(pessoaRequestDTO);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PESSOA_UPDATE')")
    public Mono<ResponseEntity<Pessoa>> updatePessoa(@PathVariable UUID id, @Valid @RequestBody PessoaRequestDTO pessoaRequestDTO) {
        return pessoaService.updatePessoa(id, pessoaRequestDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PESSOA_READ')")
    public Mono<Page<PessoaResponseDTO>> getPessoaFilter(PessoaSearchRequestDTO filter,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "createdAt") String sortBy,
                                                         @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return pessoaService.findAllByPessoasFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PESSOA_READ')")
    public Mono<ResponseEntity<PessoaResponseDTO>> getPessoaById(@PathVariable UUID id){
        return pessoaService.getPessoaById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PESSOA_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePessoa(@PathVariable UUID id) {
        return pessoaService.deletePessoa(id);
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('PESSOA_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePessoasBatch(@RequestBody List<UUID> ids) {
        return pessoaService.deletePessoasBatch(ids);
    }
}