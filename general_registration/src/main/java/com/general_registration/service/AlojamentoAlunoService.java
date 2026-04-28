package com.general_registration.service;

import com.general_registration.dto.AlojamentoAlunoRequestDTO;
import com.general_registration.dto.AlojamentoAlunoResponseDTO;
import com.general_registration.model.AlojamentoAluno;
import com.general_registration.repository.AlojamentoAlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlojamentoAlunoService {

    private final AlojamentoAlunoRepository alojamentoAlunoRepository;

    @Transactional
    public Mono<AlojamentoAluno> createAlojamentoAluno(AlojamentoAlunoRequestDTO dto) {
        return alojamentoAlunoRepository
                .existsByIdAccommodationAndIdStudent(dto.idAccommodation(), dto.idStudent())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new RuntimeException("Aluno já existe para esta acomodação."));
                    }
                    return alojamentoAlunoRepository.save(dtoToAlojamentoAluno(dto));
                });
    }

    private AlojamentoAluno dtoToAlojamentoAluno(AlojamentoAlunoRequestDTO alojamentoAlunoRequestDTO) {
        return AlojamentoAluno.builder().id(alojamentoAlunoRequestDTO.id())
                .idAccommodation(alojamentoAlunoRequestDTO.idAccommodation())
                .idStudent(alojamentoAlunoRequestDTO.idStudent())
                .entryDate(alojamentoAlunoRequestDTO.entryDate())
                .exitDate(alojamentoAlunoRequestDTO.exitDate())
                .build();
    }

    @Transactional
    public Mono<AlojamentoAluno> updateAlojamentoAluno(UUID id, AlojamentoAlunoRequestDTO dto) {
        return alojamentoAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento do Aluno não encontrado.")))
                .flatMap(existing -> Mono.when(validateUniqueAlojamentoAluno(dto, id))
                .then(Mono.defer(() -> {
                    existing.setIdAccommodation(dto.idAccommodation());
                    existing.setIdStudent(dto.idStudent());
                    existing.setEntryDate(dto.entryDate());
                    existing.setExitDate(dto.exitDate());
                    return alojamentoAlunoRepository.save(existing);
                })));
    }

    private Mono<Void> validateUniqueAlojamentoAluno(AlojamentoAlunoRequestDTO dto, UUID id) {
        return alojamentoAlunoRepository.findByIdAccommodationAndIdStudent(dto.idAccommodation(), dto.idStudent())
                .flatMap( alojamentoAluno -> {
                    if (alojamentoAluno != null && !alojamentoAluno.getId().equals(id)) {
                        return Mono.error(new RuntimeException("Este aluno já está acomodado em um alojamento por favor verifique as acomodação."));
                    }
                    return Mono.empty();
                });

    }

    public Mono<AlojamentoAlunoResponseDTO> findAlojamentoAlunoById(UUID id) {
        return alojamentoAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento do Aluno não encontrado.")))
                .map(this::alojamentoAlunoToResponseDTO);
    }

    private AlojamentoAlunoResponseDTO alojamentoAlunoToResponseDTO(AlojamentoAluno alojamentoAluno) {
        return new AlojamentoAlunoResponseDTO(
                alojamentoAluno.getId(),
                alojamentoAluno.getIdAccommodation(),
                alojamentoAluno.getIdStudent(),
                alojamentoAluno.getEntryDate(),
                alojamentoAluno.getExitDate()
        );
    }

    @Transactional
    public Mono<Void> deleteAlojamentoAluno(UUID id) {
        return alojamentoAlunoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Alojamento do Aluno não encontrado.")))
                .flatMap(alojamentoAlunoRepository::delete);
    }

    @Transactional
    public Mono<Void> deleteAlojamentoAlunoBatch(List<UUID> ids) {
        return alojamentoAlunoRepository.findAllById(ids)
                .collectList()
                .flatMap( alojamentoAlunos -> {
                    if (alojamentoAlunos.size() != ids.size()) {
                        return Mono.error(new RuntimeException("Um ou mais alojamentos do aluno não encontrados para os IDs fornecidos"));
                    }
                    return alojamentoAlunoRepository.deleteAll(alojamentoAlunos);
                });
    }
}
