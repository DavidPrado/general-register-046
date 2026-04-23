package com.general_registration.scheduled;

import com.general_registration.model.DashboardStats;
import com.general_registration.repository.DashboardRepository;
import com.general_registration.repository.EstagioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DashboardUpdaterScheduler {

    private final DashboardRepository repository;
    private final EstagioRepository estagioRespository;

    public DashboardUpdaterScheduler(DashboardRepository repository, EstagioRepository estagioRespository) {
        this.repository = repository;
        this.estagioRespository = estagioRespository;
    }

    @Scheduled(cron = "0 0 */4 * * *")
    //@Scheduled(cron = "*/15 * * * * *") // A cada 4 horas
    public void refreshDashboardData() {
        LocalDate hoje = LocalDate.now();
        LocalDate proximoMes = LocalDate.now().plusDays(30);

        Mono<DashboardStats> ativos = estagioRespository.countEstagiosAtivos(hoje)
                .map(count -> DashboardStats.builder()
                        .metricName("Estágios Ativos")
                        .metricValue(count.intValue())
                        .category("OPERACIONAL")
                        .updatedAt(LocalDateTime.now())
                        .build());

        Mono<DashboardStats> vencendo = estagioRespository.countEstagiosVencendo(hoje, proximoMes)
                .map(count -> DashboardStats.builder()
                        .metricName("Vencendo em 30 dias")
                        .metricValue(count.intValue())
                        .category("ALERTA")
                        .updatedAt(LocalDateTime.now())
                        .build());

        repository.deleteAll()
                .thenMany(Flux.concat(ativos, vencendo))
                .flatMap(repository::save)
                .doOnComplete(() -> System.out.println("Dashboard atualizado com sucesso!"))
                .subscribe();

    }

}
