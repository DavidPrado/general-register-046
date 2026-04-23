package com.general_registration.controller;

import com.general_registration.dto.NotificacaoResponseDTO;
import com.general_registration.enums.NotificationCategory;
import com.general_registration.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/notificacao")
@RequiredArgsConstructor
@Slf4j
public class NotificacaoController {

    private final NotificacaoService notificationService;


    @GetMapping("/user/{recipientId}")
    public Flux<NotificacaoResponseDTO> getByUser(
            @PathVariable UUID recipientId,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return notificationService.getNotificacaoByUser(
                recipientId,
                isRead,
                PageRequest.of(page, size)
        );
    }

    @GetMapping
    public Mono<Page<NotificacaoResponseDTO>> getNotificacoes(
            @RequestParam(required = false) UUID recipientId,
            @RequestParam(required = false) NotificationCategory category,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        List<Integer> allowedSizes = List.of(5, 10);
        int finalSize = allowedSizes.contains(size) ? size : 10;

        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));

        return notificationService.findAllByFilters(recipientId, category, isRead, pageable);
    }

    @PatchMapping("/{id}/read")
    public Mono<ResponseEntity<NotificacaoResponseDTO>> markAsRead(@PathVariable UUID id) {
        return notificationService.markAsRead(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{recipientId}/unread-count")
    public Mono<Long> countUnread(@PathVariable UUID recipientId) {
        return notificationService.countUnread(recipientId, null, false);
    }
}
