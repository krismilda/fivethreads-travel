package lt.fivethreads.services;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripCancellation;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.entities.request.NotificationDTO;

import java.util.List;

public interface NotificationService {
    //void sendNotificationsCreatedTrip (Trip trip);
    void tripAccepted(AcceptedTrip acceptedTrip);
    void tripCancelled(CancelledTrip cancelledTrip);
    void createNotifications (Trip trip, String name);
    List<NotificationDTO> getNotificationsByEmail(String email);
    void deactivateNotification(Long id);
    NotificationDTO getNotificationByID(Long notification_id);
    void createNotificationForTripMember(TripMember tripMember, String name);
}
