package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Notification;
import com.mourad.school_management.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
    List<Notification> findByRecipientIdAndType(Long recipientId, NotificationType type);
    List<Notification> findByRecipientIdAndReadFalse(Long recipientId);
    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}