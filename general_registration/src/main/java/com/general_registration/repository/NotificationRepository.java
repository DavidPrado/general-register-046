package com.general_registration.repository;

import com.general_registration.model.Notificacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface NotificationRepository extends R2dbcRepository<Notificacao, UUID> {

    Flux<Notificacao> findByRecipientIdAndReadFalseOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);

    @Query("""
                SELECT * FROM notifications 
                WHERE (recipient_id = :userId OR is_global = TRUE)
                AND (:category IS NULL OR category = :category)
                AND (:isRead IS NULL OR is_read = :isRead)
                AND (expires_at > NOW() OR expires_at IS NULL)
                ORDER BY created_at DESC
                LIMIT :limit OFFSET :offset
            """)
    Flux<Notificacao> findAllVisibleWithFilters(
            UUID userId,
            String category,
            Boolean isRead,
            int limit,
            long offset
    );


    @Query("""
                SELECT * FROM notifications 
                WHERE (recipient_id = :userId OR is_global = TRUE)
                AND (:readStatus IS NULL OR is_read = :readStatus)
                AND (expires_at > NOW() OR expires_at IS NULL)
                ORDER BY created_at DESC
                LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
            """)
    Flux<Notificacao> findAllVisibleByUserWithFilter(UUID userId, Boolean readStatus, Pageable pageable);

    @Query("""
                SELECT COUNT(*) FROM notifications
                WHERE (recipient_id = :userId OR is_global = TRUE)
                AND (:category IS NULL OR category = :category)
                AND (:isRead IS NULL OR is_read = :isRead)
                AND (expires_at > NOW() OR expires_at IS NULL)
            """)
    Mono<Long> countVisibleWithFilters(UUID userId, String category, Boolean isRead);
}
