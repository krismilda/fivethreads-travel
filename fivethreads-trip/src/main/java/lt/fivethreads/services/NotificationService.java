package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.entities.request.Notifications.*;

import java.util.List;

public interface NotificationService {
    void tripAccepted(AcceptedTrip acceptedTrip);
    void tripCancelled(CancelledTrip cancelledTrip);
    List<NotificationListDTO> getUserNotification(String email);
    List<NotificationListDTO> getOrganizerNotification(String email);
    void deactivateNotification(Long id);
    NotificationInformationChanged getNotificationByIDForInformationChanged(Long notification_id, String email);
    NotificationCancelled getNotificationByIDForCancelled(Long notification_id, String email);
    NotificationApproved getNotificationByIDForApproved(Long notification_id, String email);
    NotificationForApprovalDTO getNotificationByIDForApproval(Long notification_id, String email);
    NotificationTripDeleted getNotificationByIDDeleted(Long notification_id, String email);

}

