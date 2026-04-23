package com.general_registration.service;

import com.general_registration.dto.PessoaRequestDTO;
import com.general_registration.dto.PessoaResponseDTO;
import com.general_registration.dto.PessoaSearchRequestDTO;
import com.general_registration.matchers.PessoaMatchers;
import com.general_registration.model.Pessoa;
import com.general_registration.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Transactional
    public Mono<PessoaResponseDTO> createPessoa(PessoaRequestDTO request) {

        String cleanCpf = request.cpf().replaceAll("[.-]", "");
        String cleanRg = request.rg().replaceAll("[.-]", "");

        return validateCpfUniqueness(cleanCpf)
                .then(validateEmailUniqueness(request.email()))
                .then(validateRgUniqueness(cleanRg))
                .then(Mono.defer(() -> {
                    Pessoa pessoa = Pessoa.builder()
                            .name(request.name())
                            .rg(cleanRg)
                            .cpf(cleanCpf)
                            .email(request.email())
                            .dateOfBirth(request.dateOfBirth())
                            .phoneHome(request.phoneHome())
                            .phoneMobile(request.phoneMobile())
                            .phoneWork(request.phoneWork())
                            .uf(request.uf())
                            .city(request.city())
                            .neighborhood(request.neighborhood())
                            .street(request.street())
                            .number(request.number())
                            .complement(request.complement())
                            .build();
                    return pessoaRepository.save(pessoa);
                })).map(this::toPessoaResponseDTO);

    }

    public Mono<Page<PessoaResponseDTO>> findAllByPessoasFilter(PessoaSearchRequestDTO dto, Pageable pageable) {
        Pessoa pessoa = applyFilters(dto);
        ExampleMatcher matcher = PessoaMatchers.listPessoaFilter();
        Example<Pessoa> example = Example.of(pessoa, matcher);

        Mono<List<PessoaResponseDTO>> data = pessoaRepository.findAll(example,
                pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toPessoaResponseDTO)
                .collectList();

        Mono<Long> total = pessoaRepository.count(example);

        return Mono.zip(data, total).map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private PessoaResponseDTO toPessoaResponseDTO(Pessoa pessoa) {
        return new PessoaResponseDTO(pessoa.getId(), pessoa.getName(), pessoa.getRg(), pessoa.getCpf(), pessoa.getEmail(), pessoa.getDateOfBirth(), pessoa.getPhoneHome(), pessoa.getPhoneMobile(), pessoa.getPhoneWork(), pessoa.getUf(), pessoa.getCity(), pessoa.getNeighborhood(), pessoa.getStreet(), pessoa.getNumber(), pessoa.getComplement());
    }

    private Pessoa applyFilters(PessoaSearchRequestDTO dto) {
        return Pessoa.builder().name(dto.name()).rg(dto.rg()).cpf(dto.cpf()).email(dto.email()).dateOfBirth(dto.dateOfBirth()).phoneHome(dto.phoneHome()).phoneMobile(dto.phoneMobile()).phoneWork(dto.phoneWork()).uf(dto.uf()).city(dto.city()).neighborhood(dto.neighborhood()).street(dto.street()).number(dto.number()).complement(dto.complement()).build();
    }

    @Transactional
    public Mono<Pessoa> updatePessoa(UUID id, PessoaRequestDTO request) {
        return pessoaRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Pessoa não encontrada"))).flatMap(pessoa -> {
            return Mono.when(validateEmailUniqueness(pessoa, request), validateCpfUniqueness(pessoa, request)).then(Mono.defer(() -> updateAndSavePesso(pessoa, request)));

        });
    }

    private Mono<Void> validateCpfUniqueness(Pessoa pessoa, PessoaRequestDTO dto) {
        if (!pessoa.getCpf().equals(dto.cpf())) {
            return pessoaRepository.existsByCpf(dto.cpf()).flatMap(exists -> {
                if (exists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CPF já está em uso por outra pessoa"));
                }
                return Mono.empty();
            });
        }
        return Mono.empty();

    }

    private Mono<Void> validateCpfUniqueness(String cpf) {
        return pessoaRepository.existsByCpf(cpf).flatMap(exists -> {
            if (exists) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CPF já está em uso por outra pessoa"));
            }
            return Mono.empty();
        });
    }

    private Mono<Void> validateEmailUniqueness(String email) {
        return pessoaRepository.existsByEmail(email).flatMap(exists -> {
            if (exists) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Email já está em uso por outra pessoa"));
            }
            return Mono.empty();
        });
    }

    private Mono<Void> validateRgUniqueness(String rg) {
        return pessoaRepository.existsByRg(rg).flatMap(exists -> {
            if (exists) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "RG já está em uso por outra pessoa"));
            }
            return Mono.empty();
        });
    }

    private Mono<Void> validateEmailUniqueness(Pessoa pessoa, PessoaRequestDTO dto) {
        if (!pessoa.getEmail().equals(dto.email())) {
            return pessoaRepository.existsByEmail(dto.email()).flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Email já está em uso por outra pessoa"));
                }
                return Mono.empty();
            });
        }
        return Mono.empty();
    }

    private Mono<Pessoa> updateAndSavePesso(Pessoa pessoa, PessoaRequestDTO request) {
        pessoa.setName(request.name());
        pessoa.setRg(request.rg().replace(".", "").replace("-", ""));
        pessoa.setCpf(request.cpf().replace(".", "").replace("-", ""));
        pessoa.setEmail(request.email());
        pessoa.setDateOfBirth(request.dateOfBirth());
        pessoa.setPhoneHome(request.phoneHome());
        pessoa.setPhoneMobile(request.phoneMobile());
        pessoa.setPhoneWork(request.phoneWork());
        pessoa.setUf(request.uf());
        pessoa.setCity(request.city());
        pessoa.setNeighborhood(request.neighborhood());
        pessoa.setStreet(request.street());
        pessoa.setNumber(request.number());
        pessoa.setComplement(request.complement());
        return pessoaRepository.save(pessoa);
    }

    public Mono<PessoaResponseDTO> getPessoaById(UUID id) {
        return pessoaRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"))).map(this::toPessoaResponseDTO);
    }

    @Transactional
    public Mono<Void> deletePessoa(UUID id) {
        return pessoaRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"))).flatMap(pessoaRepository::delete);
    }

    @Transactional
    public Mono<Void> deletePessoasBatch(List<UUID> ids) {
        return pessoaRepository.findAllById(ids).collectList().flatMap(pessoas -> {
            if (pessoas.size() != ids.size()) {
                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Uma ou mais pessoas não foram encontradas"));
            }
            return pessoaRepository.deleteAll(pessoas);
        });
    }
}
