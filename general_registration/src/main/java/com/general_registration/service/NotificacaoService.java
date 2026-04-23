package com.general_registration.service;

import com.general_registration.dto.NotificacaoRequestDTO;
import com.general_registration.dto.NotificacaoResponseDTO;
import com.general_registration.enums.NotificationCategory;
import com.general_registration.model.Notificacao;
import com.general_registration.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificacaoService {

    private final NotificationRepository notificationRepository;

    public NotificacaoService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Flux<NotificacaoResponseDTO> getNotificacaoByUser(UUID idUser,Boolean isRead, Pageable pageable) {
        return notificationRepository.findAllVisibleByUserWithFilter(idUser,isRead, pageable)
                .map(this::toNotificacaoResponseDTO);
    }

    private NotificacaoResponseDTO toNotificacaoResponseDTO(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getTitle(),
                notificacao.getMessage(),
                notificacao.getCategory(),
                notificacao.getActionUrl(),
                notificacao.getRecipientId(),
                notificacao.isRead(),
                notificacao.getCreatedAt(),
                notificacao.getReadAt(),
                notificacao.getExpiresAt()
        );
    }

    @Transactional
    public Mono<NotificacaoResponseDTO> markAsRead(UUID id) {
        return notificationRepository.findById(id)
                .flatMap(notification -> {
                    Notificacao updatedNotification = new Notificacao(
                            notification.getId(),
                            notification.getTitle(),
                            notification.getMessage(),
                            notification.getCategory(),
                            notification.getActionUrl(),
                            notification.getRecipientId(),
                            true,
                            notification.getCreatedAt(),
                            OffsetDateTime.now(),
                            notification.getExpiresAt(),
                            notification.isGlobal(),
                            notification.getSystemRule()
                    );
                    return notificationRepository.save(updatedNotification);
                })
                .map(this::toNotificacaoResponseDTO);
    }

    public Mono<Long> countUnread(UUID recipientId, String category, Boolean isRead) {
        return notificationRepository.countVisibleWithFilters(recipientId, category, isRead);
    }

    @Transactional
    public Mono<Void> createNotification(NotificacaoRequestDTO notificacaoDTO) {
        Notificacao notificacao = new Notificacao(
                null,
                notificacaoDTO.title(),
                notificacaoDTO.message(),
                notificacaoDTO.category(),
                notificacaoDTO.actionUrl(),
                notificacaoDTO.recipientId(),
                false,
                OffsetDateTime.now(),
                null,
                notificacaoDTO.expiresAt(),
                notificacaoDTO.isGlobal(),
                notificacaoDTO.systemRule()
        );
        return notificationRepository.save(notificacao).then();
    }


    public Mono<Page<NotificacaoResponseDTO>> findAllByFilters(
            UUID recipientId,
            NotificationCategory category,
            Boolean isRead,
            Pageable pageable) {

        int pageSize = Math.min(pageable.getPageSize(), 10);
        String categoryStr = (category != null) ? category.getValue() : null;


        Mono<List<NotificacaoResponseDTO>> data = notificationRepository.findAllVisibleWithFilters(
                recipientId,
                categoryStr,
                isRead,
                pageSize,
                pageable.getOffset()
        ).map(this::toNotificacaoResponseDTO).collectList();


        Mono<Long> total = notificationRepository.countVisibleWithFilters(recipientId, categoryStr, isRead);

        return Mono.zip(data, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

}
