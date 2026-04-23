package com.general_registration.dto;

import com.general_registration.enums.NotificationCategory;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificacaoRequestDTO(UUID id,
                                    String title,
                                    String message,
                                    NotificationCategory category,
                                    String actionUrl,
                                    UUID recipientId,
                                    boolean isRead,
                                    OffsetDateTime createdAt,
                                    OffsetDateTime readAt,
                                    OffsetDateTime expiresAt,
                                    boolean isGlobal,
                                    String systemRule
) {
}
