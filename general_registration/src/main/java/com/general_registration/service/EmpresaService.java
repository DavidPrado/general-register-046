package com.general_registration.service;

import com.general_registration.dto.EmpresaRequestDTO;
import com.general_registration.dto.EmpresaResponseDTO;
import com.general_registration.matchers.EmpresaMatchers;
import com.general_registration.model.Empresa;
import com.general_registration.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Transactional
    public Mono<Void> createEmpresa(EmpresaRequestDTO request) {
        return empresaRepository.existsByCnpj(request.cnpj())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado")))
                .map(exists -> dtoToEmpresa(request))
                .flatMap(empresaRepository::save)
                .then();
    }

    private Empresa dtoToEmpresa(EmpresaRequestDTO request){
        return Empresa.builder()
                .name(request.name())
                .cnpj(request.cnpj())
                .email(request.email())
                .phone(request.phone())
                .uf(request.uf())
                .city(request.city())
                .neighborhood(request.neighborhood())
                .street(request.street())
                .cep(request.cep())
                .number(request.number())
                .complement(request.complement())
                .stateRegistration(request.stateRegistration())
                .municipalRegistration(request.municipalRegistration())
                .corporateName(request.corporateName())
                .fantasyName(request.fantasyName())
                .cnae(request.cnae())
                .build();
    }

    public  Mono<Page<EmpresaResponseDTO>> findAllByEmpresaFilter(EmpresaRequestDTO request, Pageable pageable){
        Empresa empresa = dtoToEmpresa(request);
        ExampleMatcher matcher = EmpresaMatchers.listEmpresaFilter();
        Example<Empresa> example = Example.of(empresa, matcher);

        Mono<List<EmpresaResponseDTO>> data = empresaRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toEmpresaResponseDTO)
                .collectList();

        Mono<Long> total = empresaRepository.count(example);

        return Mono.zip(data, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    private EmpresaResponseDTO toEmpresaResponseDTO(Empresa empresa){
        return new EmpresaResponseDTO(empresa.getId(),
                empresa.getName() ,
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getPhone(),
                empresa.getUf(),
                empresa.getCity(),
                empresa.getNeighborhood(),
                empresa.getStreet(),
                empresa.getCep(),
                empresa.getNumber(),
                empresa.getComplement(),
                empresa.getStateRegistration(),
                empresa.getMunicipalRegistration(),
                empresa.getCorporateName(),
                empresa.getFantasyName(),
                empresa.getCnae()
        );
    }

    @Transactional
    public Mono<Empresa> updateEmpresa(UUID id, EmpresaRequestDTO requestDTO){
        return empresaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Empresa não encontrada")))
                .flatMap( empresa -> {
                    return Mono.when( validateCnpjUniqueness(requestDTO, empresa.getId()))
                            .then(Mono.defer(() -> updateEmpresa(empresa, requestDTO)));
                });
    }

    private Mono<Empresa> updateEmpresa(Empresa empresa, EmpresaRequestDTO requestDTO){
        empresa.setName(requestDTO.name());
        empresa.setCnpj(requestDTO.cnpj());
        empresa.setEmail(requestDTO.email());
        empresa.setPhone(requestDTO.phone());
        empresa.setUf(requestDTO.uf());
        empresa.setCity(requestDTO.city());
        empresa.setNeighborhood(requestDTO.neighborhood());
        empresa.setStreet(requestDTO.street());
        empresa.setCep(requestDTO.cep());
        empresa.setNumber(requestDTO.number());
        empresa.setComplement(requestDTO.complement());
        empresa.setStateRegistration(requestDTO.stateRegistration());
        empresa.setMunicipalRegistration(requestDTO.municipalRegistration());
        empresa.setCorporateName(requestDTO.corporateName());
        empresa.setFantasyName(requestDTO.fantasyName());
        empresa.setCnae(requestDTO.cnae());

        return empresaRepository.save(empresa);
    }

    private Mono<Void> validateCnpjUniqueness(EmpresaRequestDTO requestDTO, UUID idAtual) {
        return empresaRepository.existsByCnpjAndIdNot(requestDTO.cnpj(), idAtual)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.CONFLICT, "Este CNPJ já está em uso por outra empresa"));
                    }
                    return Mono.empty();
                });
    }

    public Mono<EmpresaResponseDTO> getEmpresaById(UUID id) {
        return empresaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Empresa não encontrada")))
                .map(this::toEmpresaResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteEmpresa(UUID id) {
        return empresaRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Empresa não encontrada")))
                .flatMap(empresaRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteEmpresaBatch(List<UUID> ids) {
        return empresaRepository.findAllById(ids)
                .collectList()
                .flatMap( empresas -> {
                    if(empresas.size() != ids.size()){
                        return Mono.error( new ResponseStatusException(HttpStatus.NOT_FOUND, "Uma ou mais empresas não foram encontradas para os IDs fornecidos"));
                    }
                    return empresaRepository.deleteAll(empresas);
                });
    }
}
