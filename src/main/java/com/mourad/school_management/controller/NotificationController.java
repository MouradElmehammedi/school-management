package com.mourad.school_management.controller;


import com.mourad.school_management.entity.NotificationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationDTO notificationDTO) {
        return new ResponseEntity<>(notificationService.createNotification(notificationDTO),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<NotificationResponseDTO> getNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotification(id));
    }

    @GetMapping("/recipient/{recipientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByRecipient(
            @PathVariable Long recipientId) {
        return ResponseEntity.ok(notificationService.getNotificationsByRecipient(recipientId));
    }

    @GetMapping("/recipient/{recipientId}/unread")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotificationsByRecipient(
            @PathVariable Long recipientId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByRecipient(recipientId));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByType(
            @PathVariable NotificationType type) {
        return ResponseEntity.ok(notificationService.getNotificationsByType(type));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(notificationService.getNotificationsByDateRange(startDate, endDate));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/recipient/{recipientId}/read-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long recipientId) {
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/recipient/{recipientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAllNotificationsByRecipient(@PathVariable Long recipientId) {
        notificationService.deleteAllNotificationsByRecipient(recipientId);
        return ResponseEntity.noContent().build();
    }
}
