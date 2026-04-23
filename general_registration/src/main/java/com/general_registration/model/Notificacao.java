package com.general_registration.model;

import com.general_registration.enums.NotificationCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notificacao {

    @Id
    private UUID id;
    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;
    private String actionUrl;
    private UUID recipientId;
    private boolean isRead;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime expiresAt;
    private boolean isGlobal;
    private String systemRule;
}
