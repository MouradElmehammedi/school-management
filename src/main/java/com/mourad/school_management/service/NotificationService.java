package com.mourad.school_management.service;

import com.mourad.school_management.dto.NotificationDTO;
import com.mourad.school_management.dto.NotificationResponseDTO;
import com.mourad.school_management.entity.Notification;
import com.mourad.school_management.entity.NotificationType;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.NotificationRepository;
import com.mourad.school_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationResponseDTO createNotification(NotificationDTO notificationDTO) {
        // Vérifier si le destinataire existe
        User recipient = userRepository.findById(notificationDTO.getRecipientId())
                .orElseThrow(() -> new EntityNotFoundException("Recipient not found"));

        // Créer la notification
        Notification notification = Notification.builder()
                .title(notificationDTO.getTitle())
                .message(notificationDTO.getMessage())
                .type(notificationDTO.getType())
                .recipient(recipient)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notification = notificationRepository.save(notification);
        return mapToResponseDTO(notification);
    }

    public NotificationResponseDTO getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        return mapToResponseDTO(notification);
    }

    public List<NotificationResponseDTO> getNotificationsByRecipient(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> getUnreadNotificationsByRecipient(Long recipientId) {
        return notificationRepository.findByRecipientIdAndIsReadFalse(recipientId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> getNotificationsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return notificationRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        notification.setIsRead(true);
        notification = notificationRepository.save(notification);
        return mapToResponseDTO(notification);
    }

    public void markAllAsRead(Long recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientIdAndIsReadFalse(recipientId);
        notifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notificationRepository.delete(notification);
    }

    public void deleteAllNotificationsByRecipient(Long recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientId(recipientId);
        notificationRepository.deleteAll(notifications);
    }

    private NotificationResponseDTO mapToResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .recipientName(notification.getRecipient().getFirstname() + " " +
                        notification.getRecipient().getLastname())
                .recipientEmail(notification.getRecipient().getEmail())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
