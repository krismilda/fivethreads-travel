package lt.fivethreads.services;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripCancellation;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;

import java.util.List;

public interface NotificationService {
    void checkNotification(Notification notification);
    //void sendNotificationsCreatedTrip (Trip trip);
    void tripAccepted(AcceptedTrip acceptedTrip);
    void tripCancelled(CancelledTrip cancelledTrip);
}
