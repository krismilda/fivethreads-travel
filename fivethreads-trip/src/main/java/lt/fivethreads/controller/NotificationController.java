package lt.fivethreads.controller;

import lt.fivethreads.entities.request.NotificationDTO;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/user/notifications/{email}")
    @PreAuthorize("hasRole('USER')")
    public List<NotificationDTO> getAllNotifications(@PathVariable("email") String email){
        return notificationService.getNotificationsByEmail(email);
    }
}
