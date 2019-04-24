package lt.fivethreads.controller;

import lt.fivethreads.entities.request.Notifications.*;
import lt.fivethreads.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/user/notifications")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('USER')")
    public List<NotificationListDTO> getAllNotifications(){
        Collection authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ORGANIZER"))) {
            return notificationService.getOrganizerNotification(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER"))) {
            return notificationService.getUserNotification(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return null;
    }

    @GetMapping("/user/notifications/approved/{notification_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public NotificationApproved getNotificationByIDApproved(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForApproved(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
    @GetMapping("/user/notifications/ForApproval/{notification_id}")
    @PreAuthorize("hasRole('USER')")
    public NotificationForApprovalDTO getNotificationByIDforApproval(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForApproval(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }

    @GetMapping("/user/notifications/Cancelled/{notification_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public NotificationCancelled getNotificationByIDCancelled(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForCancelled(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
    @GetMapping("/user/notifications/InfoChanged/{notification_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public NotificationInformationChanged getNotificationByIDInfoChanged(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForInformationChanged(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
    @PutMapping("/user/notification/deactivate/{notification_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deactivateNotification(@PathVariable("notification_id") Long notification_id) {
        notificationService.deactivateNotification(notification_id);
        return new ResponseEntity<>("Notification deactivated successfully!", HttpStatus.OK);
    }
}
