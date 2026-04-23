package com.general_registration.service;

import com.general_registration.dto.EscolaRequestDTO;
import com.general_registration.dto.EscolaResponseDTO;
import com.general_registration.matchers.EscolaMatchers;
import com.general_registration.model.Escola;
import com.general_registration.repository.EscolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepository escolaRepository;


    @Transactional
    public Mono<Void> createEmpresa(EscolaRequestDTO request) {
        return escolaRepository.existsByCode(request.code())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Código já cadastrado")))
                .map(exists -> dtoToEscola(request))
                .flatMap(escolaRepository::save)
                .then();
    }

    private Escola dtoToEscola(EscolaRequestDTO request) {
        return Escola.builder()
                .code(request.code())
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .uf(request.uf())
                .city(request.city())
                .neighborhood(request.neighborhood())
                .street(request.street())
                .number(request.number())
                .complement(request.complement())
                .build();
    }

    public Flux<EscolaResponseDTO> findAllByEscolaFilter(EscolaRequestDTO request, Pageable pageable) {
        Escola escola = dtoToEscola(request);
        ExampleMatcher matcher = EscolaMatchers.listEscolaFilter();
        Example<Escola> example = Example.of(escola, matcher);

        return escolaRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::escolaToResponseDTO);
    }

    private EscolaResponseDTO escolaToResponseDTO(Escola escola) {
        return new EscolaResponseDTO(escola.getId(),
                escola.getCode(),
                escola.getName(),
                escola.getEmail(),
                escola.getPhone(),
                escola.getUf(),
                escola.getCity(),
                escola.getNeighborhood(),
                escola.getStreet(),
                escola.getNumber(),
                escola.getComplement());
    }

    @Transactional
    public Mono<Escola> updateEscola(UUID id, EscolaRequestDTO request) {
        return escolaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada")))
                .flatMap(escola -> {
                    return Mono.when(validadeCodigoEscola(escola, request))
                            .then(Mono.defer(() -> updateEscola(escola, request)));
                });
    }

    private Mono<Void> validadeCodigoEscola(Escola escola, EscolaRequestDTO requestDTO) {
        return escolaRepository.existsByCodeAndIdNot(requestDTO.code(), escola.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Código da escola já está em uso por outra escola"));
                    }
                    return Mono.empty();
                });
    }


    private Mono<Escola> updateEscola(Escola escola, EscolaRequestDTO request) {
        escola.setCode(request.code());
        escola.setName(request.name());
        escola.setEmail(request.email());
        escola.setPhone(request.phone());
        escola.setUf(request.uf());
        escola.setCity(request.city());
        escola.setNeighborhood(request.neighborhood());
        escola.setStreet(request.street());
        escola.setNumber(request.number());
        escola.setComplement(request.complement());

        return escolaRepository.save(escola);
    }

    public Mono<EscolaResponseDTO> findById(UUID id) {
        return escolaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada")))
                .map(this::escolaToResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteEscola(UUID id) {
        return escolaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada")))
                .flatMap(escolaRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteEscolaBatch(List<UUID> ids){
        return escolaRepository.findAllById(ids)
                .collectList()
                .flatMap( escola ->{
                    if(escola.size() != ids.size()){
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Uma ou mais escolas não foram encontradas"));
                    }
                    return escolaRepository.deleteAll(escola);
                });
    }
}
