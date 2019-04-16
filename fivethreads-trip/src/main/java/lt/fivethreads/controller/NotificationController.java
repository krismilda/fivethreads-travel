package lt.fivethreads.controller;

import lt.fivethreads.entities.request.NotificationDTO;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/user/notifications")
    @PreAuthorize("hasRole('USER')")
    public List<NotificationDTO> getAllNotifications(){
        return notificationService.getNotificationsByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/user/notifications/{notification_id}")
    @PreAuthorize("hasRole('USER')")
    public NotificationDTO getNotificationByID(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByID(notification_id);
    }

    @PutMapping("/user/notification/deactivate/{notification_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deactivateNotification(@PathVariable("notification_id") Long notification_id) {
        notificationService.deactivateNotification(notification_id);
        return new ResponseEntity<>("Notification deactivated successfully!", HttpStatus.OK);
    }
}
