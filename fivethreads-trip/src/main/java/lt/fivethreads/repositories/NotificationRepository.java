package lt.fivethreads.repositories;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

public interface NotificationRepository {
    void saveNotification(Notification notification);
    List<Notification> getAllUserNotificationByEmail(String email);
    List<Notification> getAllOrganizerNotificationByEmailPage(String email, int from, int amount);
    Notification getNotificationByID(Long id);
    void updateNotification(Notification notification);
    void deleteUser(User user);
    long getCountNotificationByEmailUser(String email);
    long getCountNotificationByEmailOrganizer(String email);
    List<Notification> getAllUserNotificationByEmailPage(String email, int from, int amount);
}
