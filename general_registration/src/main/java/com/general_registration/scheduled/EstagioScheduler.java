package com.general_registration.scheduled;

import com.general_registration.config.SecurityService;
import com.general_registration.dto.NotificacaoRequestDTO;
import com.general_registration.enums.NotificationCategory;
import com.general_registration.model.Aluno;
import com.general_registration.model.Estagio;
import com.general_registration.model.Pessoa;
import com.general_registration.repository.AlunoRepository;
import com.general_registration.repository.EstagioRepository;
import com.general_registration.repository.PessoaRepository;
import com.general_registration.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EstagioScheduler {

    private final EstagioRepository estagioRepository;
    private final AlunoRepository alunoRepository;
    private final NotificacaoService notificacaoService;
    private final PessoaRepository pessoaRepository;
    private final SecurityService securityService;

    //@Scheduled(cron = "*/15 * * * * *") //15 segundos
    @Scheduled(cron = "0 0 0/2 * * *")
    public void processarEncerramentoEstagios() {
        log.info("Iniciando processo de encerramento de estágios...");
        LocalDate hoje = LocalDate.now();

        estagioRepository.findEstagiosVencidosComDados(hoje)
                .flatMap(dados -> {
                    NotificacaoRequestDTO notificacaoRequestDTO = new NotificacaoRequestDTO(
                            null,
                            "Finalização de Estágio",
                            "O aluno " + dados.alunoNome() + " teve seu estágio encerrado por vencimento.",
                            NotificationCategory.INFO,
                            null,
                            null,
                            false,
                            OffsetDateTime.now(),
                            null,
                            OffsetDateTime.now().plusDays(3),
                            true,
                            "Processo automático de encerramento de estágio"
                    );

                    return notificacaoService.createNotification(notificacaoRequestDTO)
                            .doOnSuccess(n -> log.info("Notificação gerada para o aluno: {}", dados.alunoNome()))
                            .thenReturn(dados); // Mantemos o fluxo passando os dados
                })
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) return Mono.empty();
                    return estagioRepository.encerrarEstagiosVencidosEmLote(hoje);
                })
                .doOnError(error -> log.error("Erro ao processar encerramento: {}", error.getMessage()))
                .subscribe();
    }
}
