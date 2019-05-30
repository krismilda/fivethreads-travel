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

    @GetMapping("/notifications/page={page}/amount={amount}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('USER') or hasRole('ADMIN') ")
    public NotificationListFullDTO getAllNotifications(@PathVariable("page") int page, @PathVariable("amount") int amount){
        Collection authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ORGANIZER"))) {
            return notificationService.getOrganizerNotification(SecurityContextHolder.getContext().getAuthentication().getName(), page, amount);
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER")||r.toString().equals("ROLE_ADMIN"))) {
            return notificationService.getUserNotification(SecurityContextHolder.getContext().getAuthentication().getName(), page, amount);
        }
        return null;
    }

    @GetMapping("/notifications/Approved/{notification_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public NotificationApproved getNotificationByIDApproved(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForApproved(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
    @GetMapping("/notifications/ForApproval/{notification_id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public NotificationForApprovalDTO getNotificationByIDForApproval(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForApproval(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }

    @GetMapping("/notifications/Cancelled/{notification_id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public NotificationCancelled getNotificationByIDCancelled(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForCancelled(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
    @GetMapping("/notifications/InformationChanged/{notification_id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public NotificationInformationChanged getNotificationByIDInfoChanged(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDForInformationChanged(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
    @PutMapping("/notification/deactivate/{notification_id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<NotificationListDTO> deactivateNotification(@PathVariable("notification_id") Long notification_id) {
        NotificationListDTO notificationListDTO = notificationService.deactivateNotification(notification_id);
        return new ResponseEntity<NotificationListDTO>(notificationListDTO, HttpStatus.OK);
    }

    @GetMapping("/notifications/Deleted/{notification_id}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public NotificationTripDeleted getNotificationByIDDeleted(@PathVariable("notification_id") Long notification_id){
        return notificationService.getNotificationByIDDeleted(notification_id,SecurityContextHolder.getContext().getAuthentication().getName() );
    }
}
