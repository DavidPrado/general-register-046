package com.general_registration.service;

import com.general_registration.dto.FuncionarioRequestDTO;
import com.general_registration.dto.FuncionarioResponseDTO;
import com.general_registration.matchers.FuncionarioMatchers;
import com.general_registration.model.Funcionario;
import com.general_registration.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public Mono<Void> createFuncionario(FuncionarioRequestDTO requestDTO) {
        return funcionarioRepository.save(dtoToFuncionario(requestDTO)).then();
    }

    private Funcionario dtoToFuncionario(FuncionarioRequestDTO requestDTO) {
        return Funcionario.builder()
                .idPerson(requestDTO.idPerson())
                .idEnterprise(requestDTO.idEnterprise())
                .idPositionEnterprise(requestDTO.idPositionEnterprise())
                .build();
    }

    public Mono<Page<FuncionarioResponseDTO>> findAllByFuncionarioFilter(FuncionarioRequestDTO requestDTO, Pageable pageable) {
        Funcionario funcionario = dtoToFuncionario(requestDTO);
        ExampleMatcher matcher = FuncionarioMatchers.listFuncionarioFilter();
        Example<Funcionario> example = Example.of(funcionario, matcher);

        Mono<List<FuncionarioResponseDTO>> data = funcionarioRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toFuncionarioResponseDTO)
                .collectList();

        Mono<Long> total = funcionarioRepository.count(example);

        return Mono.zip(data, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    public Flux<FuncionarioResponseDTO> findByEmployeeNameOrCpf(FuncionarioRequestDTO requestDTO, Pageable pageable) {
        return Mono.just(requestDTO)
                .filter(request -> request.query() != null && !request.query().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("O termo de busca (query) é obrigatório para esta consulta.")))
                .flatMapMany(request -> funcionarioRepository.findByEmployeeNameOrCpf(request.query(), request.idEnterprise(), pageable));


    }

    private FuncionarioResponseDTO toFuncionarioResponseDTO(Funcionario funcionario) {
        return new FuncionarioResponseDTO(funcionario.getId(),
                funcionario.getIdPerson(),
                funcionario.getIdEnterprise(),
                funcionario.getIdPositionEnterprise(),
                null,
                null);
    }

    @Transactional
    public Mono<Funcionario> updateFuncionario(FuncionarioRequestDTO requestDTO, UUID id) {
        return funcionarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Funcionário não encontrado")))
                .flatMap(existingFuncionario -> {
                    existingFuncionario.setIdPerson(requestDTO.idPerson());
                    existingFuncionario.setIdEnterprise(requestDTO.idEnterprise());
                    existingFuncionario.setIdPositionEnterprise(requestDTO.idPositionEnterprise());
                    return funcionarioRepository.save(existingFuncionario);
                });
    }

    public Mono<FuncionarioResponseDTO> getFuncionarioById(UUID id) {
        return funcionarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Funcionário não encontrado")))
                .map(this::toFuncionarioResponseDTO);
    }

    public Mono<FuncionarioResponseDTO> getFuncionarioByIdPerson(UUID idPerson) {
        return funcionarioRepository.findByIdPerson(idPerson)
                .map(this::toFuncionarioResponseDTO);
    }

    @Transactional
    public Mono<Void> deleteFuncionario(UUID id) {
        return funcionarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Funcionário não encontrado")))
                .flatMap(funcionarioRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteFuncionarioBatch(List<UUID> ids) {
        return funcionarioRepository.findAllById(ids)
                .collectList()
                .flatMap(funcionarios -> {
                    if (funcionarios.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Nem todos os funcionários foram encontrados para os registros: " + ids));
                    }
                    return funcionarioRepository.deleteAll(funcionarios);
                });
    }

}



