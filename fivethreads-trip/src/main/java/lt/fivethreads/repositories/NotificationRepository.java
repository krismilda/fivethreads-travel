package lt.fivethreads.repositories;

import lt.fivethreads.entities.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

public interface NotificationRepository {
    void saveNotification(Notification notification);
    List<Notification> getAllNotificationByEmail(String email);
    Notification getNotificationByID(Long id);
    void updateNotification(Notification notification);
}
