package com.mourad.school_management.dto;

import com.mourad.school_management.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private String recipientName;
    private String recipientEmail;
    private boolean isRead;
    private LocalDateTime createdAt;
}